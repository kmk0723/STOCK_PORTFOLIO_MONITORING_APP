package com.capgemini.Stock.Portfolio.Monitoring.App.dto;

import java.time.LocalDateTime;

import com.capgemini.Stock.Portfolio.Monitoring.App.model.Alert.Direction;

//for API response
public class AlertResponseDTO {
    private Long id;
    private Long userId;
    private String type;
    private Double buyPrice;
    private String stockSymbol;
    private Double threshold;
    private Direction direction;
    private Boolean isActive;
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters
    public Long getId() {
        return id;
    }

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

    public Direction getDirection() {
        return direction;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

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

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
