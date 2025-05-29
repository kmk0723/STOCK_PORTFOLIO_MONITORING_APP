package com.capgemini.Stock.Portfolio.Monitoring.App.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "portfolio")
public class Portfolio {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user ; 
    }
    
}


	


