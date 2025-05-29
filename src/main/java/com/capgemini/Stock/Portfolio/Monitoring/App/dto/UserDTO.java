package com.capgemini.Stock.Portfolio.Monitoring.App.dto;


public class UserDTO {
    private String username;
    private String email;
    private String password;
    private String role;
    private Long portfolioId;

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
