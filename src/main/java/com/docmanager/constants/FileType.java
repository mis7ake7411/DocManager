package com.docmanager.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType {
  DOCUMENT("document"),
  IMAGE("image"),
  OTHER("other");

  private final String folderName;
}
