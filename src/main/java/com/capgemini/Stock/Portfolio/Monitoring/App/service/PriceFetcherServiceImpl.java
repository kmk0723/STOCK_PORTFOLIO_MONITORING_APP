package com.capgemini.Stock.Portfolio.Monitoring.App.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.capgemini.Stock.Portfolio.Monitoring.App.model.Alert;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.AlertRepository;

@Service
public class PriceFetcherServiceImpl {

	    private final AlertRepository alertRepository;
	    
	    @Autowired
	    AlertService alertService;

	    PriceFetcherServiceImpl(AlertRepository alertRepository) {
	        this.alertRepository = alertRepository;
	    }
	    
	    
		public static HashMap<Double,String> getTotalPrice(String symbol, int quantity) throws Exception{
				
				double total=getLatestPrice(symbol)*quantity;
				String now = LocalDateTime.now().toString();
				HashMap<Double,String> datamap =new HashMap<>();
				datamap.put(total, now);
				return datamap;
			}
		
			public static HashMap<Double,String> getTotalPrice(String symbol) throws Exception{
				
				double total=getLatestPrice(symbol);
				String now = LocalDateTime.now().toString();
				HashMap<Double,String> datamap =new HashMap<>();
				datamap.put(total, now);
				return datamap;
			}


			public static double getLatestPrice(String symbol) throws Exception {
			
			
			
			String url = "https://api.twelvedata.com/price?symbol="+symbol+"&apikey=756d906150354287982c5446c03d8658";
			
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
			    String priceStr = (String) jsonObject.get("price");
			
			    if (priceStr == null) {
			        throw new IllegalArgumentException("Price not found in response");
			    }
			
			    return Double.parseDouble(priceStr);
			} catch (Exception e) {
			    System.err.println("Error parsing JSON: " + e.getMessage());
			    e.printStackTrace();
			    return -1; 
			}
			
			
			}
			
			
			// Triggers every 60 seconds and calls Evaluate Alerts

	    @Scheduled(fixedRate = 6000) 
	    public void fetchPrices() {
	        
	        List<Alert> alerts = alertRepository.findAll();
	        
	        alerts.forEach(i ->{
	        	double currentPrice = 0;
	        	try {
	                System.out.println("Fetching stock prices...");


	        		currentPrice = getLatestPrice(i.getStockSymbol());
	                System.out.println("Fetching stock prices...");

	        		double gainPercent = ((currentPrice - i.getBuyPrice()) / i.getBuyPrice()) * 100;
	        		if("ABOVE".equals(i.getDirection().toString())) {
	        	        System.out.println("Fetching stock prices...");

	            		if(currentPrice >i.getThreshold() ) {
	            			
	            			alertService.evaluateAlerts(i.getId(), i.getStockSymbol(), currentPrice, gainPercent);
	            			System.out.println("ABOVE triggered");
	            		}
	            	}else {
	            		if(currentPrice < i.getThreshold() ) {
	            			
	            			alertService.evaluateAlerts(i.getId(), i.getStockSymbol(), currentPrice, gainPercent);
	            			System.out.println("BELOW triggered");
	            		}
	            	}
	        	}catch(Exception e) {
	        		throw new RuntimeException("Could not load CurrentPrice");
	        	}
	        	
	        });
	     
	        
	        
	        
	    }

}
