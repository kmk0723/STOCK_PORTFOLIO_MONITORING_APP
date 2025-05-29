package com.capgemini.Stock.Portfolio.Monitoring.App.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.Stock.Portfolio.Monitoring.App.model.Portfolio;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

//	void save(Portfolio portfolio);

}
