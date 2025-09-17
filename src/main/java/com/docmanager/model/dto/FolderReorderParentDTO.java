package com.docmanager.model.dto;

import java.util.List;

public record FolderReorderParentDTO(List<ParentItem> items) {

  public record ParentItem(Long id, Integer parentSort){}
}

