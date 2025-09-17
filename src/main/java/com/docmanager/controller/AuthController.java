package com.docmanager.controller;

import com.docmanager.model.base.ApiResponse;
import com.docmanager.model.dto.LoginReqDTO;
import com.docmanager.model.dto.LoginRespDTO;
import com.docmanager.model.dto.RefreshReqDTO;
import com.docmanager.model.dto.RegisterReqDTO;
import com.docmanager.service.auth.AuthService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("/login")
  public ApiResponse<LoginRespDTO> login(@Valid @RequestBody LoginReqDTO loginRequest) {
    return ApiResponse.success(authService.login(loginRequest));
  }

  @PostMapping("/register")
  public ApiResponse<LoginRespDTO> register(@Valid @RequestBody RegisterReqDTO registerRequest) {
    return ApiResponse.success(authService.register(registerRequest));
  }

  @PostMapping("/refresh")
  public ApiResponse<LoginRespDTO> refresh(@Valid @RequestBody RefreshReqDTO refreshToken) {
    return ApiResponse.success(authService.refreshToken(refreshToken));
  }

  @PostMapping("/logout")
  public ApiResponse<Void> logout(@RequestBody String account) {
    authService.logout(account);
    return ApiResponse.success();
  }
}
