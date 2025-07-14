package com.docmanager.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class FolderBO {
  private Long id;
  private String folderName;
  private Long parentId;
  private Integer sortOrder;
  private Boolean deleteFlag;
}
