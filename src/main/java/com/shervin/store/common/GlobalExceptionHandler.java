package com.shervin.store.common;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleUnreadableMessage(Exception exception) {
        return  ResponseEntity.badRequest().body(
                new ErrorDto( "Invalid input data")
               );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> HandleValidationErrors(
            MethodArgumentNotValidException exception)
    {
        var errorsMap = new HashMap<String, String>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {errorsMap.put(error.getField(), error.getDefaultMessage());});
        return ResponseEntity.badRequest().body(errorsMap);
    }

}

