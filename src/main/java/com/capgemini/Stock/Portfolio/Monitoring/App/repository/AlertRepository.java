package com.capgemini.Stock.Portfolio.Monitoring.App.repository;

import com.capgemini.Stock.Portfolio.Monitoring.App.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//gives access to .save(), .findAll(), .deleteById() etc.
public interface AlertRepository extends JpaRepository<Alert, Long> {
	//SELECT * FROM alerts WHERE user_id = ? AND is_active = true
    List<Alert> findByUserIdAndIsActiveTrue(Long userId);
}
