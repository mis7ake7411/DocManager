package com.docmanager.model.vo;

import com.docmanager.model.entity.Document;
import com.docmanager.model.entity.FileStorage;
import java.time.LocalDateTime;
import java.util.List;

public record DocumentVO (
    Long id,
    String documentName,
    String documentVersion,
    LocalDateTime createdDate,
    LocalDateTime modifiedDate,
    List<FileStorageVO> fileStorages
) {
    public static DocumentVO fromEntity(Document document) {
        return new DocumentVO(
            document.getId(),
            document.getDocumentName(),
            document.getDocumentVersion(),
            document.getCreatedTime() != null ? document.getCreatedTime() : null,
            document.getModifiedTime() != null ? document.getModifiedTime() : null,
            List.of()
        );
    }

    public static DocumentVO fromEntity(Document document, List<FileStorage> fileStorages) {
        return new DocumentVO(
            document.getId(),
            document.getDocumentName(),
            document.getDocumentVersion(),
            document.getCreatedTime() != null ? document.getCreatedTime() : null,
            document.getModifiedTime() != null ? document.getModifiedTime() : null,
            fileStorages.stream()
                .map(FileStorageVO::fromEntity)
                .toList()
        );
    }

    public record FileStorageVO(
        Long id,
        String fileName,
        String extension,
        Long fileSize,
        LocalDateTime uploadTime,
        String uuid) {
        public static FileStorageVO fromEntity(FileStorage fileStorage) {
            return new FileStorageVO(
                fileStorage.getId(),
                fileStorage.getFileName(),
                fileStorage.getExtension(),
                fileStorage.getFileSize(),
                fileStorage.getUploadTime() != null ? fileStorage.getUploadTime() : null,
                fileStorage.getUuid() != null ? fileStorage.getUuid().toString() : null
            );
        }
    }
}
