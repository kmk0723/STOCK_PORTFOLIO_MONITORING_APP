package com.capgemini.Stock.Portfolio.Monitoring.App.repository;

import com.capgemini.Stock.Portfolio.Monitoring.App.model.AlertLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertLogRepository extends JpaRepository<AlertLog, Long> {
    List<AlertLog> findByAlertId(Long alertId);
}
