package com.docmanager.model.dto;

public record DocumentUpsertReqDTO(
    Long id,
    String documentName,
    String documentContent,
    Long folderId,
    Long fileStorageId,
    Boolean deleteFlag,
    String description
) {

}
