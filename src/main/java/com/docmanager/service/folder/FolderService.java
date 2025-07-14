package com.docmanager.service.folder;

import com.docmanager.model.base.PageResponse;
import com.docmanager.model.dto.FolderQueryReqDTO;
import com.docmanager.model.dto.FolderReqDTO;
import com.docmanager.model.entity.Folder;
import com.docmanager.model.vo.FolderTreeVO;
import com.docmanager.model.vo.FolderVO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public interface FolderService {
  List<FolderTreeVO> getFolderTree();

  Optional<Folder> findById(Long id);

  FolderVO createFolder(FolderReqDTO folderReqDTO);

  FolderVO updateFolder(FolderReqDTO folderReqDTO);

  void deleteFolder(Long id);

  PageResponse<FolderVO> queryFolders(FolderQueryReqDTO folderReqDTO);
}
