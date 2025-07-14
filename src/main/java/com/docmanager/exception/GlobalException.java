package com.docmanager.exception;

import com.docmanager.constants.ErrorCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GlobalException extends RuntimeException {
  private final ErrorCode errorCode;

  public GlobalException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }
}
