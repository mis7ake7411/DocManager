package com.docmanager.model.vo;

import com.docmanager.model.entity.Folder;
import java.util.List;

public record FolderTreeVO(
    Long id, String folderName, Integer parentSort, List<ChildVO> children) {

  public static FolderTreeVO fromEntity(Folder parent, List<Folder> children) {
    return new FolderTreeVO(
        parent.getId(),
        parent.getFolderName(),
        parent.getSortOrder(),
        children.stream()
            .map(c -> new ChildVO(c.getId(), c.getFolderName(), c.getSortOrder()))
            .toList()
    );
  }

  public static FolderTreeVO fromEntity(Folder parent) {
    return new FolderTreeVO(
        parent.getId(),
        parent.getFolderName(),
        parent.getSortOrder(),
        List.of()
    );
  }

  public record ChildVO(Long id, String folderName, Integer childSort) {}
}