package com.example.carmanager.service.application;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
