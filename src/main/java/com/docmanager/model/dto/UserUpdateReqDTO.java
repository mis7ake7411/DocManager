package com.docmanager.model.dto;

import com.docmanager.model.bo.UserUpsertBO;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateReqDTO(
    @NotBlank(message = "Account cannot be blank")
    String account,
    @NotBlank(message = "Username cannot be blank")
    String username,
    String password,
    Boolean enabled
) {

    public UserUpsertBO toBO() {
        return UserUpsertBO.builder()
            .account(account)
            .username(username)
            .password(password)
            .enabled(enabled)
            .build();
    }
}

