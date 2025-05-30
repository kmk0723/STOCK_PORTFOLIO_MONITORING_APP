package com.capgemini.Stock.Portfolio.Monitoring.App.service;

import java.util.List;
import java.util.Map;

import com.capgemini.Stock.Portfolio.Monitoring.App.dto.PortfolioDto;
import com.capgemini.Stock.Portfolio.Monitoring.App.dto.PortfolioSellDto;

public interface PortfolioService {
	 List<Map<String, Object>> getHoldings(String username) ;
	 String buyStock(PortfolioDto portfolioDto) ;
	 String sellStock(PortfolioSellDto portfolioSellDto);
}
