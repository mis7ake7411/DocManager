package com.docmanager.service.folder;

import com.docmanager.model.base.PageResponse;
import com.docmanager.model.dto.FolderQueryReqDTO;
import com.docmanager.model.dto.FolderUpsertReqDTO;
import com.docmanager.model.entity.Folder;
import com.docmanager.model.vo.FolderTreeVO;
import com.docmanager.model.vo.FolderVO;
import java.util.List;
import java.util.Optional;


public interface FolderService {
  /**
   * 獲取資料夾樹狀結構
   *
   * @return 資料夾樹狀結構列表
   */
  List<FolderTreeVO> getFolderTree();

  Optional<Folder> findById(Long id);

  FolderVO createFolder(FolderUpsertReqDTO folderUpsertReqDTO);

  FolderVO updateFolder(FolderUpsertReqDTO folderUpsertReqDTO);

  void deleteFolder(Long id);
  /**
   * 查詢資料夾
   *
   * @param folderReqDTO 查詢條件
   * @return 分頁資料夾列表
   */
  PageResponse<FolderVO> queryFolders(FolderQueryReqDTO folderReqDTO);
}
