package com.docmanager.model.bo;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class FileStorageBO {
  private UUID uuid;
  private String fileName;
  private long fileSize;
}
