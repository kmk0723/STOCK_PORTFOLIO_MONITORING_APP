package com.capgemini.Stock.Portfolio.Monitoring.App.dto;

import com.capgemini.Stock.Portfolio.Monitoring.App.model.Portfolio;

import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class HoldingsDto {
	
	private Long portfolioId;
	
    private String symbol;
	private int quantity;
	public double buyPrice;
	


	    public String getSymbol() {
	        return symbol;
	    }

	    public void setSymbol(String symbol) {
	        this.symbol = symbol;
	    }

	    public int getQuantity() {
	        return quantity;
	    }

	    public void setQuantity(int quantity) {
	        this.quantity = quantity;
	    }

	    public double getBuyPrice() {
	        return buyPrice;
	    }

	    public void setBuyPrice(double buyPrice) {
	        this.buyPrice = buyPrice;
	    }

	    public Long getPortfolio() {
	        return portfolioId;
	    }

	    public void setPortfolio(Long portfolioId) {
	        this.portfolioId = portfolioId;
	    }
}
