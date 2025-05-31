package com.capgemini.Stock.Portfolio.Monitoring.App.Exceptions;

public class InsufficientQuantityException extends RuntimeException {
    public InsufficientQuantityException(String message) {
        super(message);
    }
}