package com.docmanager.handler;

import com.docmanager.constants.ErrorCode;
import com.docmanager.exception.GlobalException;
import com.docmanager.model.base.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * Global exception handler for the application.
 * Handles validation errors, business logic exceptions, and system errors.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException ex) {
    FieldError firstError = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .findFirst()
        .orElse(null);

    String errorMessage = (firstError != null)
        ? firstError.getField() + ": " + firstError.getDefaultMessage()
        : ErrorCode.VALIDATION_ERROR.message();

    log.warn("[參數驗證失敗] 錯誤訊息: {}", errorMessage);
    return ResponseEntity
        .badRequest()
        .body(ApiResponse.fail(ErrorCode.VALIDATION_ERROR.code(), errorMessage));
  }

  @ExceptionHandler(GlobalException.class)
  public ResponseEntity<ApiResponse<Void>> handleBusiness(GlobalException ex) {
    log.warn("[業務邏輯異常] 錯誤碼: {}, 錯誤訊息: {}", ex.getErrorCode().code(), ex.getMessage());
    HttpStatus status = mapErrorCodeToHttpStatus(ex.getErrorCode());
    return ResponseEntity
        .status(status)
        .body(ApiResponse.fail(ex.getErrorCode().code(), ex.getMessage()));
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ApiResponse<Void>> handleFileTooLarge(MaxUploadSizeExceededException ex) {
    log.warn("[檔案過大]", ex);
    return ResponseEntity
        .status(HttpStatus.PAYLOAD_TOO_LARGE)
        .body(ApiResponse.fail(ErrorCode.FILE_TOO_LARGE.code(), ErrorCode.FILE_TOO_LARGE.message()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
    log.warn("[參數異常]", ex);
    return ResponseEntity
        .badRequest()
        .body(ApiResponse.fail(ErrorCode.SYSTEM_ERROR.code(), ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>>  handleSystemException(Exception ex) {
    log.error("[未預期系統異常]", ex);
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.fail(ErrorCode.SYSTEM_ERROR.code(), ErrorCode.SYSTEM_ERROR.message()));
  }

  private HttpStatus mapErrorCodeToHttpStatus(ErrorCode errorCode) {
    return switch (errorCode) {
      case VALIDATION_ERROR -> HttpStatus.BAD_REQUEST;
      case FILE_TOO_LARGE -> HttpStatus.PAYLOAD_TOO_LARGE;
      case NOT_FOUND -> HttpStatus.NOT_FOUND;
      case DUPLICATE_RECORD -> HttpStatus.CONFLICT;
      case SYSTEM_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
      default -> HttpStatus.BAD_REQUEST;
    };
  }
}
