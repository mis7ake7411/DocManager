package com.docmanager.model.dto;

import java.util.List;

public record LoginRespDTO(String accessToken, String refreshToken, String username, List<String> roles) {}
