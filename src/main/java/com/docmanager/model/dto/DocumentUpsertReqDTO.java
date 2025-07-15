package com.docmanager.model.dto;

import jakarta.validation.constraints.NotBlank;

public record DocumentUpsertReqDTO(
    Long id,
    String documentName,
    String documentContent,
    @NotBlank
    Long folderId,
    Long fileStorageId,
    Boolean deleteFlag,
    String description
) {

}
