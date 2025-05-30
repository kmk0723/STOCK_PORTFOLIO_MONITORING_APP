package com.capgemini.Stock.Portfolio.Monitoring.App.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.capgemini.Stock.Portfolio.Monitoring.App.dto.AlertRequestDTO;
import com.capgemini.Stock.Portfolio.Monitoring.App.dto.AlertResponseDTO;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.Alert;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.AlertLog;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.User;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.AlertLogRepository;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.AlertRepository;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AlertServiceImplTest {

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private AlertLogRepository alertLogRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AlertServiceImpl alertService;

    private User user;
    private Alert alert;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setId(1L);
        user.setUsername("Test User");

        alert = new Alert();
        alert.setId(10L);
        alert.setUserId(user.getId());
        alert.setStockSymbol("AAPL");
        alert.setThreshold(150.0);
        alert.setType("PRICE");
        alert.setIsActive(true);
    }

    @Test
    void testCreateAlert_Success() {
        AlertRequestDTO dto = new AlertRequestDTO();
        dto.setUserId(user.getId());
        dto.setStockSymbol("AAPL");
        dto.setThreshold(150.0);
        dto.setType("PRICE");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(alertRepository.save(any(Alert.class))).thenAnswer(i -> {
            Alert savedAlert = i.getArgument(0);
            savedAlert.setId(10L);
            return savedAlert;
        });

        AlertResponseDTO response = alertService.createAlert(dto);

        assertNotNull(response);
        assertEquals(user.getId(), response.getUserId());
        assertEquals("AAPL", response.getStockSymbol());
        assertEquals(150.0, response.getThreshold());
        assertEquals("PRICE", response.getType());
        assertTrue(response.getIsActive());

        verify(userRepository, times(1)).findById(user.getId());
        verify(alertRepository, times(1)).save(any(Alert.class));
    }

    @Test
    void testCreateAlert_UserNotFound() {
        AlertRequestDTO dto = new AlertRequestDTO();
        dto.setUserId(999L);

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> alertService.createAlert(dto));

        assertEquals("User not found", thrown.getMessage());
    }

    @Test
    void testGetActiveAlerts() {
        when(alertRepository.findByUserIdAndIsActiveTrue(user.getId()))
            .thenReturn(Arrays.asList(alert));

        List<AlertResponseDTO> alerts = alertService.getActiveAlerts(user.getId());

        assertEquals(1, alerts.size());
        assertEquals(alert.getStockSymbol(), alerts.get(0).getStockSymbol());

        verify(alertRepository, times(1)).findByUserIdAndIsActiveTrue(user.getId());
    }

    @Test
    void testUpdateAlert() {
        AlertRequestDTO dto = new AlertRequestDTO();
        dto.setStockSymbol("GOOGL");
        dto.setThreshold(2800.0);
        dto.setType("PRICE");

        when(alertRepository.findById(alert.getId())).thenReturn(Optional.of(alert));
        when(alertRepository.save(any(Alert.class))).thenAnswer(i -> i.getArgument(0));

        AlertResponseDTO updated = alertService.updateAlert(alert.getId(), dto);

        assertEquals("GOOGL", updated.getStockSymbol());
        assertEquals(2800.0, updated.getThreshold());
        assertTrue(updated.getIsActive());

        verify(alertRepository, times(1)).findById(alert.getId());
        verify(alertRepository, times(1)).save(any(Alert.class));
    }

    @Test
    void testGetAllLogs() {
        AlertLog log1 = new AlertLog();
        log1.setId(1L);
        AlertLog log2 = new AlertLog();
        log2.setId(2L);

        when(alertLogRepository.findAll()).thenReturn(Arrays.asList(log1, log2));

        List<AlertLog> logs = alertService.getAllLogs();

        assertEquals(2, logs.size());
        verify(alertLogRepository, times(1)).findAll();
    }

    @Test
    void testEvaluateAlerts_PriceAlertTriggered() {
        Alert alert2 = new Alert();
        alert2.setId(11L);
        alert2.setUserId(user.getId());
        alert2.setStockSymbol("AAPL");
        alert2.setThreshold(200.0);
        alert2.setType("PRICE");
        alert2.setIsActive(true);

        when(alertRepository.findByUserIdAndIsActiveTrue(user.getId()))
            .thenReturn(Arrays.asList(alert, alert2));

        alertService.evaluateAlerts(user.getId(), "AAPL", 160.0, 0.0);

        ArgumentCaptor<AlertLog> captor = ArgumentCaptor.forClass(AlertLog.class);
        verify(alertLogRepository, times(1)).save(captor.capture());

        AlertLog savedLog = captor.getValue();
        assertTrue(savedLog.getMessage().contains("price reached target"));
        assertEquals(alert.getId(), savedLog.getAlertId());
    }

    @Test
    void testEvaluateAlerts_PortfolioAlertTriggered() {
        Alert portfolioAlert = new Alert();
        portfolioAlert.setId(20L);
        portfolioAlert.setUserId(user.getId());
        portfolioAlert.setThreshold(10.0);
        portfolioAlert.setType("PORTFOLIO");
        portfolioAlert.setIsActive(true);

        when(alertRepository.findByUserIdAndIsActiveTrue(user.getId()))
            .thenReturn(Arrays.asList(portfolioAlert));

        alertService.evaluateAlerts(user.getId(), "AAPL", 100.0, 15.0);

        ArgumentCaptor<AlertLog> captor = ArgumentCaptor.forClass(AlertLog.class);
        verify(alertLogRepository, times(1)).save(captor.capture());

        AlertLog savedLog = captor.getValue();
        assertTrue(savedLog.getMessage().contains("Portfolio loss exceeded"));
        assertEquals(portfolioAlert.getId(), savedLog.getAlertId());
    }

    @Test
    void testEvaluateAlerts_NoAlertTriggered() {
        Alert alert3 = new Alert();
        alert3.setId(30L);
        alert3.setUserId(user.getId());
        alert3.setStockSymbol("MSFT");
        alert3.setThreshold(300.0);
        alert3.setType("PRICE");
        alert3.setIsActive(true);

        when(alertRepository.findByUserIdAndIsActiveTrue(user.getId()))
            .thenReturn(Arrays.asList(alert3));

        alertService.evaluateAlerts(user.getId(), "AAPL", 100.0, 5.0);

        verify(alertLogRepository, never()).save(any());
    }
}
