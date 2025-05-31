package com.capgemini.Stock.Portfolio.Monitoring.App.Exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}