package com.docmanager.constants;

public enum ErrorCode {
  SUCCESS("200", "成功"),
  VALIDATION_ERROR("400", "參數驗證失敗"),
  NOT_FOUND("404", "資源不存在"),
  BUSINESS_ERROR("409", "邏輯錯誤"),
  SYSTEM_ERROR("500", "系統錯誤");

  private final String code;
  private final String message;

  ErrorCode(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public String code() {
    return code;
  }

  public String message() {
    return message;
  }
}
