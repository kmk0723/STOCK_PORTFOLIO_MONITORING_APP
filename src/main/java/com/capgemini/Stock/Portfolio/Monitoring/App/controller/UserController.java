package com.capgemini.Stock.Portfolio.Monitoring.App.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class UserController {
	
	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome";
	}
	
}
