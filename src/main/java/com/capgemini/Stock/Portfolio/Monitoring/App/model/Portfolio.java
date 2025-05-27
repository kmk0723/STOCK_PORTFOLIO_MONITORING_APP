package com.capgemini.Stock.Portfolio.Monitoring.App.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="portfolios")
public class Portfolio {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String name;
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	private LocalDateTime createdAt = LocalDateTime.now();
	
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id=id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name=name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description=description;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user=user;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt=createdAt;
	}
	

}
