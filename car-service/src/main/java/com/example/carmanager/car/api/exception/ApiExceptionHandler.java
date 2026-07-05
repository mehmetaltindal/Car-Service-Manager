package com.example.carmanager.car.api.exception;

import com.example.carmanager.car.application.exception.ConflictException;
import com.example.carmanager.car.application.exception.ValidationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    record ErrorResponse(int status, String error, String message) {
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ErrorResponse conflict(ConflictException ex) {
        return new ErrorResponse(409, "Conflict", ex.getMessage());
    }

    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse badRequest(Exception ex) {
        return new ErrorResponse(400, "Bad Request", ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponse notFound(EntityNotFoundException ex) {
        return new ErrorResponse(404, "Not Found", ex.getMessage());
    }
}
