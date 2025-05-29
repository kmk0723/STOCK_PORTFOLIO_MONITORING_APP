package com.capgemini.Stock.Portfolio.Monitoring.App.dto;

public class AlertRequestDTO {
    private Long userId;
    private String type;
    private Double buyPrice;
    private String stockSymbol;
    private Double threshold;
    private String direction;

    // Getters and setters
    public Long getUserId() {
        return userId;
    }

    public String getType() {
        return type;
    }

    public Double getBuyPrice() {
        return buyPrice;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public Double getThreshold() {
        return threshold;
    }

    public String getDirection() {
        return direction;
    }

    // Setters
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}