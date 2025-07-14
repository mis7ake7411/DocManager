package com.docmanager.model.base;

import com.docmanager.constants.ErrorCode;

public record ApiResponse<T>(String code, String message, T data) {

  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(ErrorCode.SUCCESS.code(), ErrorCode.SUCCESS.message(), data);
  }

  public static ApiResponse<Void> success() {
    return new ApiResponse<>(ErrorCode.SUCCESS.code(), ErrorCode.SUCCESS.message(), null);
  }

  public static ApiResponse<Void> fail(String code, String message) {
    return new ApiResponse<>(code, message, null);
  }
}
