package com.innowise.songmanager.contractapi.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private HttpStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    private String message;

    private String debugMessage;

    private List<ApiSubError> subErrors;

    public ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status) {
        this();
        this.status = status;
    }

    public ApiError(HttpStatus status, Throwable throwable) {
        this(status);
        this.message = "Unexpected error";
        this.debugMessage = throwable.getMessage();
    }

    public ApiError(HttpStatus status, String message, Throwable throwable) {
        this(status);
        this.message = message;
        this.debugMessage = throwable.getMessage();
    }

    private void addSubError(ApiSubError subError) {
        if (subErrors == null) {
            subErrors = new ArrayList<>();
        }
        subErrors.add(subError);
    }

    public void addValidationFieldErrors(List<FieldError> fieldErrors) {
        for (FieldError fieldError : fieldErrors) {
            ApiSubError subError = new ApiValidationError(
                fieldError.getObjectName(),
                fieldError.getField(),
                fieldError.getRejectedValue(),
                fieldError.getDefaultMessage()
            );

            addSubError(subError);
        }
    }

    public void addValidationGlobalErrors(List<ObjectError> globalErrors) {
        for (ObjectError fieldError : globalErrors) {
            ApiSubError subError = new ApiValidationError(
                fieldError.getObjectName(),
                fieldError.getDefaultMessage()
            );

            addSubError(subError);
        }
    }
}
