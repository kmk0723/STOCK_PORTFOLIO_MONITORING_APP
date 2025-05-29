package com.capgemini.Stock.Portfolio.Monitoring.App.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.Stock.Portfolio.Monitoring.App.model.Holding;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.Portfolio;

public interface HoldingsRepository extends JpaRepository<Holding, Long>{
	List<Holding> findByPortfolio(Portfolio portfolio);
    Optional<Holding> findByPortfolioAndSymbol(Portfolio portfolio, String symbol);

}
