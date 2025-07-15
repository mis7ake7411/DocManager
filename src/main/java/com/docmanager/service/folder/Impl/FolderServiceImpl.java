package com.docmanager.service.folder.Impl;

import com.docmanager.model.base.PageResponse;
import com.docmanager.model.bo.FolderBO;
import com.docmanager.model.dto.FolderQueryReqDTO;
import com.docmanager.model.dto.FolderUpsertReqDTO;
import com.docmanager.model.entity.Folder;
import com.docmanager.model.vo.FolderTreeVO;
import com.docmanager.model.vo.FolderVO;
import com.docmanager.repository.folderManager.FolderRepository;
import com.docmanager.service.folder.FolderService;
import com.docmanager.specifications.FolderSpec;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {
  private final FolderRepository folderRepository;

  @Override
  public List<FolderTreeVO> getFolderTree() {
    // 查找所有父層級資料夾
    Set<Folder> parents = folderRepository.findByParentIdIsNullAndDeleteFlagFalseOrderBySortOrderAsc();
    if (parents.isEmpty()) {
      return List.of();
    }
    // 查找所有子層級資料夾
    List<Long> parentIds = parents.stream()
        .map(Folder::getId)
        .toList();

    // 使用父層級資料夾ID查找子層級資料夾
    Set<Folder> children = folderRepository.findByParentIdInAndDeleteFlagFalseOrderBySortOrderAsc(parentIds);
    log.info("找到 {} 個父層級資料夾，{} 個子層級資料夾", parents.size(), children.size());
    // 如果沒有子層級資料夾，則返回父層級資料夾列表
    if (children.isEmpty()) {
      return parents.stream()
          .map(FolderTreeVO::fromEntity)
          .toList();
    }
    // 將子層級資料夾按父層級ID分組
    Map<Long, List<Folder>> childrenMap = children.stream()
        .filter(folder -> !folder.getDeleteFlag() && folder.getParent() != null)
        .collect(Collectors.groupingBy(folder -> folder.getParent().getId()));
    // 將父層級資料夾轉換為樹狀結構
    return parents.stream()
        .map(parent -> {
          List<Folder> childFolders = childrenMap.getOrDefault(parent.getId(), List.of());
          return FolderTreeVO.fromEntity(parent, childFolders);
        })
        .toList();
  }

  @Override
  public Optional<Folder> findById(Long id) {
    return folderRepository.findById(id)
        .filter(folder -> !folder.getDeleteFlag())
        .map(folder -> {
          folder.setChildren(folderRepository.findByParentIdAndDeleteFlagFalseOrderBySortOrderAsc(folder.getId()));
          return folder;
        });
  }

  @Override
  public FolderVO createFolder(FolderUpsertReqDTO folderUpsertReqDTO) {
    // 檢查是否有重複的資料夾名稱
    if (folderRepository.existsByFolderNameAndParentIdAndDeleteFlagFalse(
        folderUpsertReqDTO.folderName(), folderUpsertReqDTO.parentId())) {
      throw new IllegalArgumentException("資料夾名稱重複");
    }
    Folder newFolder = new Folder();
    // 檢查父層級資料夾是否存在，並設定 parent
    if (folderUpsertReqDTO.parentId() != null) {
      Folder parent = folderRepository.findByIdAndDeleteFlagFalse(folderUpsertReqDTO.parentId())
          .orElseThrow(() -> new IllegalArgumentException("父層資料夾不存在"));
      newFolder.setParent(parent);
    }
    // 設置新資料夾的屬性
    newFolder.setFolderName(folderUpsertReqDTO.folderName());
    newFolder.setSortOrder(folderUpsertReqDTO.sortOrder() != null ? folderUpsertReqDTO.sortOrder() : 1);
    newFolder.setCreatedTime(LocalDateTime.now());
    newFolder.setDeleteFlag(false);
    // 保存新資料夾
    Folder savedFolder = folderRepository.save(newFolder);
    // 返回資料夾的值對象
    return FolderVO.fromEntity(savedFolder);
  }

  @Override
  public FolderVO updateFolder(FolderUpsertReqDTO folderUpsertReqDTO) {
    // 查找要更新的資料夾
    Folder folder = folderRepository.findByIdAndDeleteFlagFalse(folderUpsertReqDTO.id())
        .orElseThrow(() -> new IllegalArgumentException("資料夾不存在"));

    // 更新資料夾屬性
    folder.setFolderName(folderUpsertReqDTO.folderName());
    folder.setSortOrder(folderUpsertReqDTO.sortOrder());
    folder.setModifiedTime(LocalDateTime.now());

    // 保存更新後的資料夾
    Folder savedFolder = folderRepository.save(folder);

    // 返回更新後的資料夾值對象
    return FolderVO.fromEntity(savedFolder);
  }

  @Override
  public void deleteFolder(Long id) {
    // 查找要刪除的資料夾
    Folder folder = folderRepository.findByIdAndDeleteFlagFalse(id)
        .orElseThrow(() -> new IllegalArgumentException("資料夾不存在"));

    // 設置刪除標誌為 true
    folder.setDeleteFlag(true);

    // 保存更新後的資料夾
    folderRepository.save(folder);
  }

  @Override
  public PageResponse<FolderVO> queryFolders(FolderQueryReqDTO folderReqDTO) {
    FolderBO bo = folderReqDTO.toBO();
    Specification<Folder> spec = FolderSpec.dynamic(bo);
    Pageable pageable = PageResponse.getPageable(folderReqDTO.page(), folderReqDTO.pageSize());

    Page<Folder> folderPage = folderRepository.findAll(spec, pageable);

    List<FolderVO> folderVOList = folderPage.getContent().stream()
        .map(FolderVO::fromEntity)
        .toList();

    return PageResponse.of(folderVOList, folderPage);
  }
}
