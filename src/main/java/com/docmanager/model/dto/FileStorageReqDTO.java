package com.docmanager.model.dto;

import com.docmanager.model.bo.FileStorageBO;
import java.util.UUID;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;

public record FileStorageReqDTO(
    String uuid,
    String fileName,
    Long fileSize
) {

  public FileStorageBO toBO() {
    return FileStorageBO.builder()
        .uuid(uuid == null ? null : UUID.fromString(uuid))
        .fileName(fileName)
        .fileSize(NumberUtils.toLong(
            ObjectUtils.defaultIfNull(
                fileSize, 0L).toString(), 0L
            )
        )
        .build();
  }

  public record FileNamePartsDTO(String name, String extension) {}
}
