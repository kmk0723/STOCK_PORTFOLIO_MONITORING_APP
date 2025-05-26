package com.capgemini.Stock.Portfolio.Monitoring.App.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class controller {
	
	@GetMapping("/welcome")
	public String hello() {
		return "welcome";
	}

}
