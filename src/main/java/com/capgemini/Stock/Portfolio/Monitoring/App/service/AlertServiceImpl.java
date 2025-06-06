package com.capgemini.Stock.Portfolio.Monitoring.App.service;

import com.capgemini.Stock.Portfolio.Monitoring.App.dto.AlertRequestDTO;
import com.capgemini.Stock.Portfolio.Monitoring.App.dto.AlertResponseDTO;
import com.capgemini.Stock.Portfolio.Monitoring.App.Exceptions.AlertNotFoundException;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.Alert;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.AlertLog;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.User;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.AlertLogRepository;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.AlertRepository;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
//Tells Spring this class has business logic.
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;
    private final AlertLogRepository alertLogRepository;
    private final UserRepository userRepository;

    public AlertServiceImpl(AlertRepository alertRepository,
                            AlertLogRepository alertLogRepository,
                            UserRepository userRepository) {
        this.alertRepository = alertRepository;
        this.alertLogRepository = alertLogRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AlertResponseDTO createAlert(AlertRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Alert alert = new Alert();
        alert.setUserId(user.getId());
        alert.setBuyPrice(dto.getBuyPrice());
        alert.setDirection(dto.getDirection());
        alert.setStockSymbol(dto.getStockSymbol());
        alert.setThreshold(dto.getThreshold());
        alert.setType(dto.getType());
        alert.setIsActive(true);
        alert.setTriggered(false); // ✅ Required to avoid DB error


        Alert saved = alertRepository.save(alert);
        return mapToResponse(saved);
    }
    
//    .stream(): Turns list into a stream of elements.
//
//    .map(this::mapToResponse): Transforms each Alert → DTO.
//
//    .collect(Collectors.toList()): Rebuilds a list from the stream.

    @Override
    public List<AlertResponseDTO> getActiveAlerts(Long userId) {
        return alertRepository.findByUserIdAndIsActiveTrue(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AlertResponseDTO updateAlert(Long id, AlertRequestDTO dto) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new AlertNotFoundException("Alert not found with ID: " + id));

        alert.setStockSymbol(dto.getStockSymbol());
        alert.setThreshold(dto.getThreshold());
        alert.setType(dto.getType());
        alert.setIsActive(true);

        return mapToResponse(alertRepository.save(alert));
    }

    @Override
    public List<AlertLog> getAllLogs() {
        return alertLogRepository.findAll();
    }

    @Override
    public void evaluateAlerts(Long userId,double threshold, String stockSymbol, double currentPrice) {
        String message =stockSymbol + " price reached target of " +threshold;

    	   AlertLog log = new AlertLog();
           log.setAlertId(userId);
           log.setTriggeredAt(LocalDateTime.now());
           log.setMessage(message);
           alertLogRepository.save(log);
//        List<Alert> alerts = alertRepository.findByUserIdAndIsActiveTrue(userId);
//        System.out.println(alerts);
//        for (Alert alert : alerts) {
//            boolean triggered = false;
//            String message = "";
//
//            System.out.println(stockSymbol + " " + alert.getStockSymbol());
//            if ("PRICE".equals(alert.getType())) {
////                if (currentPrice >= alert.getThreshold()) {
//                    triggered = true;
//                    message = alert.getStockSymbol() + " price reached target of " + alert.getThreshold();
////                }
//            }
//
////            if ("PORTFOLIO".equals(alert.getType())) {
////                if (portfolioLossPercent >= alert.getThreshold()) {
////                    triggered = true;
////                    message = "Portfolio loss exceeded " + alert.getThreshold() + "%";
////                }
////            }
//
//            if (triggered) {
//                AlertLog log = new AlertLog();
//                log.setAlertId(alert.getId());
//                log.setTriggeredAt(LocalDateTime.now());
//                log.setMessage(message);
//                alertLogRepository.save(log);
//            }
//        }
    }
//Converts internal DB entity to API-safe object.
    private AlertResponseDTO mapToResponse(Alert alert) {
        AlertResponseDTO dto = new AlertResponseDTO();
        dto.setId(alert.getId());
        dto.setUserId(alert.getUserId());
        dto.setBuyPrice(alert.getBuyPrice());
        dto.setStockSymbol(alert.getStockSymbol());
        dto.setThreshold(alert.getThreshold());
        dto.setType(alert.getType());
        dto.setDirection(alert.getDirection());
        dto.setIsActive(alert.getIsActive());
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }
}
