package com.capgemini.Stock.Portfolio.Monitoring.App.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.capgemini.Stock.Portfolio.Monitoring.App.service.UserService;
import java.util.Map;
import com.capgemini.Stock.Portfolio.Monitoring.App.dto.UserDTO;

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

    @PostMapping("/login")
    public Object login(@RequestBody Map<String, String> credentials) {
        return userService.login(credentials.get("email"), credentials.get("password"))
                .map(user -> Map.of(
                        "username", user.getUsername(),
                        "email", user.getEmail(),
                        "role", user.getRole(),
                        "portfolioId", user.getPortfolioId()
                ))
                .orElse(Map.of("error", "Invalid credentials"));
    }
}