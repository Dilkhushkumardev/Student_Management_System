package com.smartattend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public static class ErrorResponse {
        private boolean success;
        private String message;
        private String errorCode;
        private String path;
        private LocalDateTime timestamp;
        private Map<String, String> validationErrors;

        public ErrorResponse() {
        }

        public ErrorResponse(boolean success, String message, String errorCode, String path, LocalDateTime timestamp,
                Map<String, String> validationErrors) {
            this.success = success;
            this.message = message;
            this.errorCode = errorCode;
            this.path = path;
            this.timestamp = timestamp;
            this.validationErrors = validationErrors;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public Map<String, String> getValidationErrors() {
            return validationErrors;
        }

        public void setValidationErrors(Map<String, String> validationErrors) {
            this.validationErrors = validationErrors;
        }
    }

    @SuppressWarnings("null")
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                false,
                ex.getMessage(),
                ex.getErrorCode(),
                request.getRequestURI(),
                LocalDateTime.now(),
                null);
        return new ResponseEntity<>(error, ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorResponse error = new ErrorResponse(
                false,
                "Validation failed for input data",
                "VALIDATION_FAILED",
                request.getRequestURI(),
                LocalDateTime.now(),
                errors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                false,
                "Invalid username or password",
                "INVALID_CREDENTIALS",
                request.getRequestURI(),
                LocalDateTime.now(),
                null);
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                false,
                "Access is denied. You do not have permission to perform this action.",
                "ACCESS_DENIED",
                request.getRequestURI(),
                LocalDateTime.now(),
                null);
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                false,
                ex.getMessage() != null ? ex.getMessage() : "An unexpected internal server error occurred",
                "INTERNAL_SERVER_ERROR",
                request.getRequestURI(),
                LocalDateTime.now(),
                null);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
