package com.capgemini.Stock.Portfolio.Monitoring.App.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.Stock.Portfolio.Monitoring.App.dto.PortfolioDto;
import com.capgemini.Stock.Portfolio.Monitoring.App.dto.PortfolioSellDto;
import com.capgemini.Stock.Portfolio.Monitoring.App.service.PortfolioService;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    private PortfolioService portfolioService;
    
    public PortfolioController(PortfolioService portfolioService) {
    	this.portfolioService = portfolioService;
    }

    @GetMapping("/{username}")
    public Object getHoldings(@PathVariable String username) {
        return portfolioService.getHoldings(username);
    }
    
    @GetMapping("/showStocks")
    public ResponseEntity<?> showStocks() {
    	
    	try {
    		
		String url = "https://api.twelvedata.com/cryptocurrency_exchanges";
		
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setRequestMethod("GET");
		
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
		    content.append(inputLine);
		}
		
		in.close();
		conn.disconnect();
		
		try {
		    JSONParser parser = new JSONParser();
		    JSONObject jsonObject = (JSONObject) parser.parse(content.toString());
		  
		
		    return ResponseEntity.ok(jsonObject);
		    
		    
		} catch (Exception e) {
		    System.err.println("Error parsing JSON: " + e.getMessage());
		    e.printStackTrace();
		    return ResponseEntity.badRequest().body(-1);
		}
    	} catch (Exception e) {
			return ResponseEntity.badRequest().body("CouldN't fetch");
		}

		
    }

    @PostMapping("/buy")
    public String buyStock(@RequestBody PortfolioDto portfolioDto) {

        return portfolioService.buyStock(portfolioDto.getUsername(), portfolioDto.getSymbol(), portfolioDto.getQuantity(), portfolioDto.getBuyPrice());
    }

    @PutMapping("/sell")
    public String sellStock(@RequestBody PortfolioSellDto  portfolioSellDto ) {

        return portfolioService.sellStock(portfolioSellDto.getUsername(), portfolioSellDto.getSymbol(), portfolioSellDto.getQuantity());
    }
}
