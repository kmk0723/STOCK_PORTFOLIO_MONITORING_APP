package com.capgemini.Stock.Portfolio.Monitoring.App.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity 
@Table(name = "stock_price_cache")
public class StockPriceCache {
	@Id 
	private String stockSymbol;
	
	private double price;
	private LocalDateTime lastUpdated;
	
	public StockPriceCache() {
		
	}
	public StockPriceCache(String stockSymbol, double price, LocalDateTime lastUpdated) {
		this.stockSymbol = stockSymbol;
		this.price = price;
		this.lastUpdated= lastUpdated;
	}
	
	public String getStockSymbol() {
		return stockSymbol;
	}
	
	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price =price;
	}
	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

}
