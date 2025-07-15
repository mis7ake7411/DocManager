package com.docmanager.model.dto;

import jakarta.validation.constraints.NotNull;

public record DocumentUpsertReqDTO(
    Long id,
    String documentName,
    String documentContent,
    @NotNull
    Long folderId,
    Long fileStorageId,
    Boolean deleteFlag,
    String description
) {

}
