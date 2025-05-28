package com.capgemini.Stock.Portfolio.Monitoring.App.service;

import java.util.List;

import com.capgemini.Stock.Portfolio.Monitoring.App.dto.HoldingsDto;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.Holding;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.Portfolio;

public interface IHoldingsService {
	
	public Holding addHoldings(HoldingsDto holdingsDto);
	public List<Holding> getHoldings();
//	Holding updateHoldings(HoldingsDto holdingsDto,long id);
	
	
}
