package com.capgemini.Stock.Portfolio.Monitoring.App.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserDTO {
	
	private Long id;
    private String username;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
//    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$", message = "Only Gmail addresses are allowed")
    private String email;
    private String password;
    private String role;
    private Long portfolioId;
    public Long getId() { 
    	return id; 
    }
    
    public void setId(Long id) { 
    	this.id = id;
    }
    
    // Getters and Setters
    public String getUsername() { 
    	return username; 
    }
    
    public void setUsername(String username) { 
    	this.username = username; 
    }

    public String getEmail() { 
    	return email; 
    }
    
    public void setEmail(String email) { 
    	this.email = email;
    }

    public String getPassword() { 
    	return password; 
    }
    
    public void setPassword(String password) { 
    	this.password = password; 
    }

    public String getRole() { 
    	return role; 
    }
    
    public void setRole(String role) { 
    	this.role = role; 
    }

    public Long getPortfolioId() { 
    	return portfolioId; 
    }
    
    public void setPortfolioId(Long portfolioId) { 
    	this.portfolioId = portfolioId; 
    }
}
