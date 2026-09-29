package com.mustafizur.hibernateadvanced.api;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.LinkedHashMap;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail bodyValidation(MethodArgumentNotValidException ex) {
        var errors = new LinkedHashMap<String, String>();
        ex.getBindingResult().getFieldErrors().forEach(e -> errors.putIfAbsent(e.getField(), e.getDefaultMessage()));
        ex.getBindingResult().getGlobalErrors().forEach(e -> errors.putIfAbsent("$", e.getDefaultMessage()));
        return validationProblem(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ProblemDetail constraintValidation(ConstraintViolationException ex) {
        var errors = new LinkedHashMap<String, String>();
        ex.getConstraintViolations().forEach(v -> errors.putIfAbsent(v.getPropertyPath().toString(), v.getMessage()));
        return validationProblem(errors);
    }

    private ProblemDetail validationProblem(Object errors) {
        var detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "One or more validation constraints failed");
        detail.setTitle("Validation failed");
        detail.setType(URI.create("urn:problem:validation"));
        detail.setProperty("errors", errors);
        return detail;
    }
}
