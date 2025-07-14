package com.docmanager.model.dto;

import com.docmanager.model.bo.FolderBO;

public record FolderQueryReqDTO (
    Long id,
    String folderName,
    Long parentId,
    Boolean deleteFlag,
    Integer page,
    Integer pageSize
) {
  public FolderQueryReqDTO {
    if (page == null || page < 1) {
      page = 1;
    }
    if (pageSize == null || pageSize < 1) {
      pageSize = 10;
    }
  }

  public FolderBO toBO() {
    return FolderBO.builder()
        .id(id)
        .folderName(folderName)
        .parentId(parentId)
        .deleteFlag(deleteFlag != null && deleteFlag)
        .build();
  }
}
