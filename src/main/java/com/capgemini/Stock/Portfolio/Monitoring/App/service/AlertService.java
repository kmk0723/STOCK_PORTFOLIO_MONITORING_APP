package com.capgemini.Stock.Portfolio.Monitoring.App.service;

import java.util.List;

import com.capgemini.Stock.Portfolio.Monitoring.App.dto.AlertRequestDTO;
import com.capgemini.Stock.Portfolio.Monitoring.App.dto.AlertResponseDTO;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.Alert;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.AlertLog;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.User;

import java.util.List;

public interface AlertService {
    AlertResponseDTO createAlert(AlertRequestDTO dto);
    List<AlertResponseDTO> getActiveAlerts(Long userId);
    AlertResponseDTO updateAlert(Long id, AlertRequestDTO dto);
    List<AlertLog> getAllLogs();
    void evaluateAlerts(Long userId, String stockSymbol, double currentPrice);
}