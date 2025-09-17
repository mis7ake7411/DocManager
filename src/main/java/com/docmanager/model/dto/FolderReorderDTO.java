package com.docmanager.model.dto;

import java.util.List;

public record FolderReorderDTO(Long parentId, List<ChildrenItem> childrenItems) {

  public record ChildrenItem(Long id, Integer childSort) {
  }
}

