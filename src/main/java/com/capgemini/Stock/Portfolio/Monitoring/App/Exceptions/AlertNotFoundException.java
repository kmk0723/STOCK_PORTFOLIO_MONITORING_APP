package com.capgemini.Stock.Portfolio.Monitoring.App.Exceptions;

public class AlertNotFoundException extends RuntimeException {
    public AlertNotFoundException(String message) {
        super(message);
    }
}