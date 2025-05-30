package com.capgemini.Stock.Portfolio.Monitoring.App.service;

import com.capgemini.Stock.Portfolio.Monitoring.App.model.Holding;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface ReportService {
    List<Holding> getPortfolioSummary(String email);
    ByteArrayInputStream exportToExcel(String email) throws Exception;
}
