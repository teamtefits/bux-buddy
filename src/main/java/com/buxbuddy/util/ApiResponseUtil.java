package com.buxbuddy.util;

import com.buxbuddy.auth.dto.api.ApiSuccessResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;

public final class ApiResponseUtil {
    private ApiResponseUtil() {
        // Prevent object creation
    }
    public static <T> ResponseEntity<ApiSuccessResponse<T>> success(
            HttpStatus status,
            String message,
            T data,
            HttpServletRequest request) {

        ApiSuccessResponse<T> response = ApiSuccessResponse.<T>builder()
                .status(status.value())
                .message(message)
                .data(data)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(status)
                .body(response);
    }
}