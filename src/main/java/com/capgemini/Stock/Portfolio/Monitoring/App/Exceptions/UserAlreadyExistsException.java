package com.capgemini.Stock.Portfolio.Monitoring.App.Exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}