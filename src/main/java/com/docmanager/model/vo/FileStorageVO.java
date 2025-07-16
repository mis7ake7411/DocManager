package com.docmanager.model.vo;

import com.docmanager.model.entity.FileStorage;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record FileStorageVO (
    Long id,
    String uuid,
    String fileName,
    String extension,
    String fullFileName,
    String filePath,
    Long fileSize,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime uploadTime) {

    public static FileStorageVO fromEntity(FileStorage fileStorage) {
        return new FileStorageVO(
            fileStorage.getId(),
            fileStorage.getUuid() != null ? fileStorage.getUuid().toString() : null,
            fileStorage.getFileName(),
            fileStorage.getExtension(),
            String.format("%s.%s", fileStorage.getFileName(), fileStorage.getExtension()),
            fileStorage.getFilePath(),
            fileStorage.getFileSize(),
            fileStorage.getUploadTime() != null ? fileStorage.getUploadTime() : null
        );
    }
}
