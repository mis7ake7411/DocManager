package com.docmanager.handler;

import com.docmanager.constants.ErrorCode;
import com.docmanager.exception.GlobalException;
import com.docmanager.model.base.ApiResponse;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for the application.
 * Handles validation errors, business logic exceptions, and system errors.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ApiResponse<Void> handleValidationException(MethodArgumentNotValidException ex) {
    FieldError firstError = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .findFirst()
        .orElse(null);

    String errorMessage = (firstError != null)
        ? firstError.getField() + ": " + firstError.getDefaultMessage()
        : ErrorCode.VALIDATION_ERROR.message();

    return ApiResponse.fail(ErrorCode.VALIDATION_ERROR.code(), errorMessage);
  }

  @ExceptionHandler(GlobalException.class)
  public ApiResponse<Void> handleBusiness(GlobalException ex) {
    return ApiResponse.fail(ex.getErrorCode().code(), ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ApiResponse<Void> handleSystemException(Exception ex) {
    return ApiResponse.fail(ErrorCode.SYSTEM_ERROR.code(), ErrorCode.SYSTEM_ERROR.message());
  }
}
