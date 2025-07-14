package com.docmanager.model.vo;

import com.docmanager.model.entity.Folder;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record FolderVO (
    Long id,
    String folderName,
    Long parentId,
    Integer sortOrder,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdTime,
    Boolean deleteFlag
) {

  public static FolderVO fromEntity(Folder savedFolder) {
    return new FolderVO(
        savedFolder.getId(),
        savedFolder.getFolderName(),
        savedFolder.getParent() != null ? savedFolder.getParent().getId() : null,
        savedFolder.getSortOrder(),
        savedFolder.getCreatedTime(),
        savedFolder.getDeleteFlag()
    );
  }
}
