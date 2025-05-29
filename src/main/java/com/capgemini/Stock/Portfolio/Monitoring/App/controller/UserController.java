package com.capgemini.Stock.Portfolio.Monitoring.App.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.User;
import com.capgemini.Stock.Portfolio.Monitoring.App.service.UserService;

import java.util.Map;
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
    	user.setRole("USER");
        return userService.register(user);
    }
    
    @PostMapping("/register-admin")
    public User registerAdmin(@RequestBody User user) {
        user.setRole("ADMIN");
        return userService.register(user);
    }


    @PostMapping("/login")
    public Object login(@RequestBody Map<String, String> credentials) {
        return userService.login(credentials.get("email"), credentials.get("password"))
                .map(user -> Map.of(
                        "username", user.getUsername(),
                        "email", user.getEmail(),
                        "role", user.getRole(),
                        "portfolioId", user.getPortfolio().getId()
                ))
                .orElse(Map.of("error", "Invalid credentials"));
    }
}