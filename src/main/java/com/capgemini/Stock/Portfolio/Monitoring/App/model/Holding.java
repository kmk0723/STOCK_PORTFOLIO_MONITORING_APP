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
	
	private String stockSymbol;
	private int quantity;
	public double buyPrice;
	
	public Long getId() {
		return id;
	}
	
	public Portfolio getPortfolio() {
		return portfolio;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public double getBuyPrice() {
		return buyPrice;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}
	
	public void setBuyPrice(double price) {
		this.buyPrice = price;
	}
}
