package com.capgemini.Stock.Portfolio.Monitoring.App.repository;

import com.capgemini.Stock.Portfolio.Monitoring.App.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByUserIdAndIsActiveTrue(Long userId);
}
