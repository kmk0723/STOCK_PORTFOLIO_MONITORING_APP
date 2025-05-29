package com.capgemini.Stock.Portfolio.Monitoring.App.dto;

public class PortfolioSellDto {
	private String username ;
	private String symbol;
	private int quantity;
	
	public String getUsername() {
		return username;
	}
	public String getSymbol() {
		return symbol;
	}
	public int getQuantity() {
		return quantity;
	}
	
	
	
	public void setUsername(String username) {
		this.username = username;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	



	
}
