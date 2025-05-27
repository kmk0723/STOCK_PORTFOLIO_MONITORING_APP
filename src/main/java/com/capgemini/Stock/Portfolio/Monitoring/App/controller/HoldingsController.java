package com.capgemini.Stock.Portfolio.Monitoring.App.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/holdings")
public class HoldingsController {
	
	@GetMapping
	public String saveHoldings() {
		return "saa";
	}
}
