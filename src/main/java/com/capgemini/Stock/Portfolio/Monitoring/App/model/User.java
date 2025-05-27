package com.capgemini.Stock.Portfolio.Monitoring.App.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)   //Specifies how Primary Key is automatically generated
	private Long id; //Primary Key
	@Column(nullable = false)
	private String username;
	@Column(nullable = false)
	private String email;        
	@Column(nullable = false)
	private String passowrd;
	@Column(nullable = false)
	private String role = "USER";
	private LocalDateTime createdTime = LocalDateTime.now();
	
	//Getters and setters to access private variables (Encapsulation)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmial() {
		return email;
	}
	public void setEmial(String email) {
		this.email = email;
	}
	public String getPassowrd() {
		return passowrd;
	}
	public void setPassowrd(String passowrd) {
		this.passowrd = passowrd;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public LocalDateTime getCreatedTime() {
		return createdTime;
	}
	public void setCreateAt(LocalDateTime createAt) {
		this.createdTime = createAt;
	}
}