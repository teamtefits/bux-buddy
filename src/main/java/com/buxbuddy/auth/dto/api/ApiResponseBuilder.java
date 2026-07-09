package com.buxbuddy.auth.dto.api;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

public class ApiResponseBuilder {
    public static <T> ApiSuccessResponse<T> success(
            int status,
            String message,
            T data,
            HttpServletRequest request) {
        return ApiSuccessResponse.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
    }
}

