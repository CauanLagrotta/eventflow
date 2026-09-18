package com.cauanlagrotta.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(GlobalExceptionHandler.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(GlobalExceptionHandler e){
        var error = new ErrorResponse(
            "Internal Server Error", 
            500, 
            "INTERNAL_ERROR",
             e.getMessage()
            );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){

        var fieldErrors = e.getFieldErrors()
            .stream()
            .map(f -> new ValidationErrorResponse.FieldError(f.getField(), f.getDefaultMessage()))
            .toList();

        String detail = fieldErrors.isEmpty() ? "Validation Field" : fieldErrors.get(0).message();

        var error = new ValidationErrorResponse(
            "Bad Request", 
            400, 
            "VALIDATION_ERROR", 
            fieldErrors
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
        var error = new ErrorResponse(
            "Bad Request",
            400,
            "INVALID_REQUEST_BODY",
            e.getMostSpecificCause().getMessage()
        );
        return ResponseEntity.badRequest().body(error);
    }

}
