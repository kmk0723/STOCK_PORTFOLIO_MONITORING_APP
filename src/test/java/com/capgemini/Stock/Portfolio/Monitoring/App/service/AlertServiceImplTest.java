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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertServiceImplTest {

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private AlertLogRepository alertLogRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AlertServiceImpl alertService;

    private AlertRequestDTO alertRequestDTO;
    private Alert alert;
    private User user;

    @BeforeEach
    void setUp() {
        alertRequestDTO = new AlertRequestDTO();
        alertRequestDTO.setUserId(1L);
        alertRequestDTO.setBuyPrice(100.0);
        alertRequestDTO.setDirection(Alert.Direction.ABOVE);
        alertRequestDTO.setStockSymbol("AAPL");
        alertRequestDTO.setThreshold(120.0);
        alertRequestDTO.setType("PRICE");

        user = new User();
        user.setId(1L);

        alert = new Alert();
        alert.setId(1L);
        alert.setUserId(1L);
        alert.setBuyPrice(100.0);
        alert.setDirection(Alert.Direction.ABOVE);
        alert.setStockSymbol("AAPL");
        alert.setThreshold(120.0);
        alert.setType("PRICE");
        alert.setIsActive(true);
    }

    @Test
    void testCreateAlert() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(alertRepository.save(any(Alert.class))).thenReturn(alert);

        AlertResponseDTO response = alertService.createAlert(alertRequestDTO);

        assertNotNull(response);
        assertEquals("AAPL", response.getStockSymbol());
        verify(alertRepository, times(1)).save(any(Alert.class));
    }

    @Test
    void testGetActiveAlerts() {
        when(alertRepository.findByUserIdAndIsActiveTrue(1L)).thenReturn(List.of(alert));

        List<AlertResponseDTO> alerts = alertService.getActiveAlerts(1L);

        assertEquals(1, alerts.size());
        assertEquals("AAPL", alerts.get(0).getStockSymbol());
    }

    @Test
    void testUpdateAlert_Success() {
        when(alertRepository.findById(1L)).thenReturn(Optional.of(alert));
        when(alertRepository.save(any(Alert.class))).thenReturn(alert);

        AlertResponseDTO updated = alertService.updateAlert(1L, alertRequestDTO);

        assertNotNull(updated);
        assertEquals("AAPL", updated.getStockSymbol());
    }

    @Test
    void testUpdateAlert_AlertNotFound() {
        when(alertRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(AlertNotFoundException.class, () -> {
            alertService.updateAlert(2L, alertRequestDTO);
        });
    }

    @Test
    void testGetAllLogs() {
        AlertLog log = new AlertLog();
        log.setId(1L);
        log.setAlertId(1L);
        log.setTriggeredAt(LocalDateTime.now());
        log.setMessage("AAPL price reached target");

        when(alertLogRepository.findAll()).thenReturn(List.of(log));

        List<AlertLog> logs = alertService.getAllLogs();

        assertEquals(1, logs.size());
        assertEquals("AAPL price reached target", logs.get(0).getMessage());
    }

    @Test
    void testEvaluateAlerts_CreatesLog() {
        when(alertLogRepository.save(any(AlertLog.class))).thenAnswer(i -> i.getArgument(0));

        alertService.evaluateAlerts(1L, 120.0, "AAPL", 125.0);

        verify(alertLogRepository, times(1)).save(any(AlertLog.class));
    }
}
