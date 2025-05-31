package com.capgemini.Stock.Portfolio.Monitoring.App.Exceptions;

public class PriceFetchException extends RuntimeException {
    public PriceFetchException(String message) {
        super(message);
    }

    public PriceFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}