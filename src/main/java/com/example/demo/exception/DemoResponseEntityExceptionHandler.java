package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@ControllerAdvice
public class DemoResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {BookNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
        Map<String, String> response = new HashMap<>();
        response.put("code", HttpStatus.NOT_FOUND.toString());
        response.put("message", ex.getMessage());
        StackTraceElement[] trace = ex.getStackTrace();
        String traceString = Arrays.stream(trace).limit(10).map(StackTraceElement::toString).collect(Collectors.joining("\n"));
        Optional.ofNullable(ex.getCause()).ifPresent(cause -> response.put("cause", cause.toString()));
        response.put("trace", traceString);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
