package com.capgemini.Stock.Portfolio.Monitoring.App.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
public class Portfolio {
	
	private long id;
	private String name;
	private String Description;
	private User user;
	private LocalDateTime createdAt = LocalDateTime.now();
	
	

}
