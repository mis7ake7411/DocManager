package com.docmanager.model.dto;

import jakarta.validation.constraints.NotBlank;

public record FolderUpsertReqDTO(
    Long id,
    @NotBlank
    String folderName,
    Long parentId,
    @NotBlank
    Integer sortOrder,
    Boolean deleteFlag
) {

}
