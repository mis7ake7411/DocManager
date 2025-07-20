package com.docmanager.exception;

import com.docmanager.constants.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GlobalException extends RuntimeException {
  private final ErrorCode errorCode;

  public GlobalException(ErrorCode errorCode) {
    super(errorCode.message());
    this.errorCode = errorCode;
  }

  public GlobalException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

}
