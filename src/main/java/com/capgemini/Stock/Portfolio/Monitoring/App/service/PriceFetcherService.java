package com.capgemini.Stock.Portfolio.Monitoring.App.service;

import java.util.HashMap;

public interface PriceFetcherService {
	 HashMap<Double,String> getTotalPrice(String symbol, int quantity) throws Exception  ;
	 HashMap<Double,String> getTotalPrice(String symbol) throws Exception ;
	  double getLatestPrice(String symbol) throws Exception;
	void fetchPrices();
}
