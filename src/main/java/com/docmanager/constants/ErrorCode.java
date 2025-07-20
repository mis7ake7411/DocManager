package com.docmanager.constants;

public enum ErrorCode {
  SUCCESS("200", "成功"),
  VALIDATION_ERROR("40001", "參數驗證失敗"),
  USER_EXISTS("40003", "使用者已存在"),
  PARAM_MISSING("40002", "缺少必要參數"),
  UNAUTHORIZED("401", "未授權"),
  NOT_FOUND("404", "資源不存在"),
  USER_NOT_FOUND("40401", "使用者不存在"),
  FILE_TOO_LARGE("40010", "上傳檔案大小超過限制"),
  DUPLICATE_RECORD("40901", "資料重複"),
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
