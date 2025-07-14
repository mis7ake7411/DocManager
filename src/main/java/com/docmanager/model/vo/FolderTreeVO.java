package com.docmanager.model.vo;

import com.docmanager.model.entity.Folder;
import java.util.List;

public record FolderTreeVO(Long id, String folderName, List<ChildVO> children) {
  public static FolderTreeVO fromEntity(Folder parent, List<Folder> children) {
    return new FolderTreeVO(
        parent.getId(),
        parent.getFolderName(),
        children.stream()
            .map(c -> new ChildVO(c.getId(), c.getFolderName()))
            .toList()
    );
  }
  public record ChildVO(Long id, String folderName) {}
}