package com.capgemini.Stock.Portfolio.Monitoring.App.service;

import java.util.HashMap;

public interface PriceFetcherService {
	 HashMap<Double,String> getTotalPrice(String symbol, int quantity) ;
	 HashMap<Double,String> getTotalPrice(String symbol);
	  double getLatestPrice(String symbol) throws Exception;
	void fetchPrices();
}
