package com.capgemini.Stock.Portfolio.Monitoring.App.service;

import com.capgemini.Stock.Portfolio.Monitoring.App.Exceptions.PriceFetchException;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.Alert;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.Alert.Direction;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.AlertRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
public class PriceFetcherServiceImpl implements PriceFetcherService{

    private final AlertRepository alertRepository;

    @Autowired
    AlertService alertService;

    PriceFetcherServiceImpl(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public HashMap<Double, String> getTotalPrice(String symbol, int quantity) throws Exception {
        double total = getLatestPrice(symbol) * quantity;
        String now = LocalDateTime.now().toString();
        HashMap<Double, String> datamap = new HashMap<>();
        datamap.put(total, now);
        return datamap;
    }

    public HashMap<Double, String> getTotalPrice(String symbol) throws Exception {
        double total = getLatestPrice(symbol);
        String now = LocalDateTime.now().toString();
        HashMap<Double, String> datamap = new HashMap<>();
        datamap.put(total, now);
        return datamap;
    }

    
    
    public double getLatestPrice(String symbol) throws Exception {
        String url = "https://api.twelvedata.com/price?symbol="+ symbol +"&apikey=756d906150354287982c5446c03d8658";
        System.out.println(url);
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        try {
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

            in.close();
            conn.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(content.toString());
            String priceStr = (String) jsonObject.get("price");

            if (priceStr == null) {
                throw new PriceFetchException("Price not found in response for symbol: " + symbol);
            }

            return Double.parseDouble(priceStr);
        } catch (Exception e) {
            throw new PriceFetchException("Failed to fetch price for symbol: " + symbol, e);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void fetchPrices() {
        System.out.println("Fetching stock prices...");
        List<Alert> alerts = alertRepository.findAll();
//        System.out.println(alerts);
        alerts.forEach(i -> {
            try {
            	double currentPrice = 0;
            	try {
            		
            		currentPrice = getLatestPrice(i.getStockSymbol());
            	}catch(Exception e) {
            		throw new RuntimeException("Could not load current Price");
            	}
                System.out.println(currentPrice);
//                double gainPercent = ((currentPrice - i.getBuyPrice()) / i.getBuyPrice()) * 100;

                if (Direction.ABOVE.equals(i.getDirection())) {
                    if (currentPrice > i.getThreshold()) {
                        alertService.evaluateAlerts(i.getId(), i.getThreshold(),i.getStockSymbol(), currentPrice);
                        System.out.println("ABOVE triggered");
                    }
                } else if (Direction.BELOW.equals(i.getDirection())) {
                    if (currentPrice < i.getThreshold()) {
                        alertService.evaluateAlerts(i.getId(),i.getThreshold(), i.getStockSymbol(), currentPrice);
                        System.out.println("BELOW triggered");
                    }
                }
            } catch (PriceFetchException e) {
                System.err.println("Error fetching price for symbol " + i.getStockSymbol() + ": " + e.getMessage());
            }
        });
    }
}
