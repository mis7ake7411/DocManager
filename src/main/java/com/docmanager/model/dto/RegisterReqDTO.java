package com.docmanager.model.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterReqDTO(
    @NotBlank(message = "Account cannot be blank")
    String account,
    @NotBlank(message = "Password cannot be blank")
    String password,
    @NotBlank(message = "Username cannot be blank")
    String username
) {}
