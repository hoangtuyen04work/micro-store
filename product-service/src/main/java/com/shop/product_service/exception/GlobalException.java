package com.shop.product_service.exception;


import com.shop.product_service.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handleAccessDeniedException(final AccessDeniedException e) {
        ErrorCode errorCode = ErrorCode.ACCESSDENIED;
        return ResponseEntity.status(errorCode.getHttpStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .build());
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleAppException(final AppException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        return ResponseEntity.status(errorCode.getHttpStatusCode())
                .body(apiResponse);
    }
}
