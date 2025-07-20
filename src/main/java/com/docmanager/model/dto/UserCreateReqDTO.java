package com.docmanager.model.dto;

import com.docmanager.model.bo.UserUpsertBO;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Set;

public record UserCreateReqDTO(
    @NotBlank(message = "Account cannot be blank")
    String account,
    @NotBlank(message = "Username cannot be blank")
    String username,
    @NotBlank(message = "Password cannot be blank")
    String password,
    Set<Long> roleIds
) {
    public UserUpsertBO toBO() {
        return UserUpsertBO.builder()
            .account(account)
            .username(username)
            .password(password)
            .enabled(true)
            .createdTime(LocalDateTime.now())
            .roleIds(roleIds)
            .build();
    }
}

