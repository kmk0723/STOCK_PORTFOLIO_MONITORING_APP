package com.capgemini.Stock.Portfolio.Monitoring.App.dto;

public class PortfolioDto {
	private String email ;
	private String symbol;
	private int quantity;
	private double buyPrice;
	
	public String getUsername() {
		return email;
	}
	public String getSymbol() {
		return symbol;
	}
	public int getQuantity() {
		return quantity;
	}
	
	public double getBuyPrice() {
		return buyPrice;
	}
	
	public void setUsername(String email) {
		this.email = email;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}

}
