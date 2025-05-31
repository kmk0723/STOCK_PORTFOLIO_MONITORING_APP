package com.capgemini.Stock.Portfolio.Monitoring.App.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.capgemini.Stock.Portfolio.Monitoring.App.service.UserService;

import java.util.List;
import java.util.Map;

import com.capgemini.Stock.Portfolio.Monitoring.App.dto.LoginDto;
import com.capgemini.Stock.Portfolio.Monitoring.App.dto.UserDTO;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.Holding;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserDTO register(@RequestBody UserDTO userDto) {
        userDto.setRole("USER");
        return userService.register(userDto);
    }

    @PostMapping("/register-admin")
    public UserDTO registerAdmin(@RequestBody UserDTO userDto) {
        userDto.setRole("ADMIN");
        return userService.register(userDto);
    }    
    @PostMapping("/{admin}/holdings")
    public ResponseEntity<?> showHoldings(@PathVariable String admin){
    	return ResponseEntity.ok(userService.showHoldings(admin));
    	 
    }
    @PostMapping("/{admin}/users")
    public ResponseEntity<?> showUsers(@PathVariable String admin){
    	return ResponseEntity.ok(userService.showUsers(admin));
    	 
    }
    @PostMapping("/login")
    public Object login(@RequestBody LoginDto credentials) {
        return userService.login(credentials.getEmail(), credentials.getPassword())
                .map(user -> Map.of(
                        "username", user.getUsername(),
                        "email", user.getEmail(),
                        "role", user.getRole(),
                        "portfolioId", user.getPortfolioId()
                ))
                .orElse(Map.of("error", "Invalid credentials"));
    }
}