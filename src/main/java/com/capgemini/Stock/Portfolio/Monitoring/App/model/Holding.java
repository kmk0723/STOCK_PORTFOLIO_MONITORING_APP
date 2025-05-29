package com.capgemini.Stock.Portfolio.Monitoring.App.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

//Entity with table name as holdings to store the holdings by the user.
@Entity
@Table(name = "holdings")
public class Holding {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne(fetch =  FetchType.LAZY)
	@JoinColumn(name="porfolio_id")
	private Portfolio portfolio;
	
    private String symbol;
	private int quantity;
	public double buyPrice;
	
	 public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

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

	    public Portfolio getPortfolio() {
	        return portfolio;
	    }

	    public void setPortfolio(Portfolio portfolio) {
	        this.portfolio = portfolio;
	    }
}
