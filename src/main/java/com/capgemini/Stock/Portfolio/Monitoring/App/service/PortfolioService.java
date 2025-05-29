package com.capgemini.Stock.Portfolio.Monitoring.App.service;

import java.util.List;
import java.util.Map;

public interface PortfolioService {
	 List<Map<String, Object>> getHoldings(String username) ;
	 String buyStock(String username, String symbol, int quantity, double buyPrice) ;
	 String sellStock(String username, String symbol, int quantity);
}
