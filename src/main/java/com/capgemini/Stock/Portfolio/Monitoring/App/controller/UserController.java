package com.capgemini.Stock.Portfolio.Monitoring.App.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.Stock.Portfolio.Monitoring.App.dto.LoginDto;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.User;
import com.capgemini.Stock.Portfolio.Monitoring.App.service.UserServiceImpl;

@RestController()
public class UserController {
	
	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome";
	}
	private final UserServiceImpl userService;

    // Constructor injection to initialize final field
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User saved = userService.registerUser(user);
            saved.setPassword(null); // Don't return password
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto logindto){
    	try {
    		String response = userService.login(logindto.getUsername(),logindto.getPassword());
    		return ResponseEntity.ok(response);
    	} catch (IllegalArgumentException e) {
    		
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    
    @GetMapping("/{username}/getAll")
    public ResponseEntity<?> userByUsername(@PathVariable String username ){
    	
    	
    	try {
    		User admin = userService.findByUsername(username);
    		
    		if(admin.getRole() == User.Role.USER) {
    			return ResponseEntity.badRequest().body("not admin");
    		}
    		List<User> users = userService.getAllUsers();
    		users.forEach(i -> i.setPassword(null));
    		return ResponseEntity.ok(users);

    		
    	} catch (IllegalArgumentException e) {
    		
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
