package com.capgemini.Stock.Portfolio.Monitoring.App.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.capgemini.Stock.Portfolio.Monitoring.App.dto.AlertRequestDTO;
import com.capgemini.Stock.Portfolio.Monitoring.App.dto.AlertResponseDTO;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.Alert;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.AlertLog;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.AlertLogRepository;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.AlertRepository;
import com.capgemini.Stock.Portfolio.Monitoring.App.service.AlertService;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {
	@Autowired
    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping
    public ResponseEntity<?> createAlert(@RequestBody AlertRequestDTO alert) {
        try {
           
            return ResponseEntity.ok( alertService.createAlert(alert));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }


    @GetMapping
    public List<AlertResponseDTO> getAlerts(@RequestParam Long userId) {
        return alertService.getActiveAlerts(userId);
    }

    @PutMapping("/{id}")
    public AlertResponseDTO updateAlert(@PathVariable Long id, @RequestBody AlertRequestDTO updated) {

    	return alertService.updateAlert(id, updated);
    	
    }

    @GetMapping("/logs")
    public List<AlertLog> getAllLogs() {
        return alertService.getAllLogs();
     }
}

