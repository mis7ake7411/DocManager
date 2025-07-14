package com.docmanager.model.vo;

import com.docmanager.model.entity.Folder;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import org.springframework.data.domain.PageImpl;

public record FolderVO (
    Long id,
    String folderName,
    Long parentId,
    Integer sortOrder,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime createdTime,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime modifiedTime,
    Boolean deleteFlag,
    Integer page,
    Integer pageSize
) {

  public static FolderVO fromEntity(Folder savedFolder) {
    return new FolderVO(
        savedFolder.getId(),
        savedFolder.getFolderName(),
        savedFolder.getParent() != null ? savedFolder.getParent().getId() : null,
        savedFolder.getSortOrder(),
        savedFolder.getCreatedTime(),
        savedFolder.getModifiedTime(),
        savedFolder.getDeleteFlag(),
        1,
        10
    );
  }

//  public static FolderVO fromEntityWithPage(Folder savedFolder, PageImpl pageImpl) {
//    return new FolderVO(
//        savedFolder.getId(),
//        savedFolder.getFolderName(),
//        savedFolder.getParent() != null ? savedFolder.getParent().getId() : null,
//        savedFolder.getSortOrder(),
//        savedFolder.getCreatedTime(),
//        savedFolder.getModifiedTime(),
//        savedFolder.getDeleteFlag(),
//        page,
//        pageSize
//    );
//  }
}
