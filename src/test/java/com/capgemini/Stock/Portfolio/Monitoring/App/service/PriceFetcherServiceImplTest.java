package com.capgemini.Stock.Portfolio.Monitoring.App.service;

import com.capgemini.Stock.Portfolio.Monitoring.App.model.Alert;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.Alert.Direction;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.AlertRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.lang.reflect.Field;
import java.util.List;

import static org.mockito.Mockito.*;

public class PriceFetcherServiceImplTest {

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private AlertService alertService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test that BELOW threshold triggers alert
     */
    @Test
    void testFetchPrices_belowThreshold() throws Exception {
        Alert alert = new Alert();
        alert.setId(2L);
        alert.setUserId(1L);
        alert.setThreshold(200.0);
        alert.setStockSymbol("GOOG");
        alert.setDirection(Direction.BELOW);
        alert.setBuyPrice(210.0);

        when(alertRepository.findAll()).thenReturn(List.of(alert));

        // Create spy after mocking repo
        PriceFetcherServiceImpl spyService = spy(new PriceFetcherServiceImpl(alertRepository));

        // Inject alertService into private field
        Field alertServiceField = PriceFetcherServiceImpl.class.getDeclaredField("alertService");
        alertServiceField.setAccessible(true);
        alertServiceField.set(spyService, alertService);

        // Simulate current price
        doReturn(190.0).when(spyService).getLatestPrice("GOOG");

        // Act
        spyService.fetchPrices();

        // Assert
        verify(alertService, times(1)).evaluateAlerts(2L, 200.0, "GOOG", 190.0);
    }

    /**
     * Test that ABOVE threshold triggers alert
     */
    @Test
    void testFetchPrices_aboveThreshold() throws Exception {
        Alert alert = new Alert();
        alert.setId(1L);
        alert.setUserId(1L);
        alert.setThreshold(150.0);
        alert.setStockSymbol("AAPL");
        alert.setDirection(Direction.ABOVE);
        alert.setBuyPrice(140.0);

        when(alertRepository.findAll()).thenReturn(List.of(alert));

        PriceFetcherServiceImpl spyService = spy(new PriceFetcherServiceImpl(alertRepository));

        Field alertServiceField = PriceFetcherServiceImpl.class.getDeclaredField("alertService");
        alertServiceField.setAccessible(true);
        alertServiceField.set(spyService, alertService);

        doReturn(160.0).when(spyService).getLatestPrice("AAPL");

        spyService.fetchPrices();

        verify(alertService, times(1)).evaluateAlerts(1L, 150.0, "AAPL", 160.0);
    }

    /**
     * Test that no alert is triggered if threshold condition isn't met
     */
    @Test
    void testFetchPrices_noTrigger() throws Exception {
        Alert alert = new Alert();
        alert.setId(3L);
        alert.setUserId(1L);
        alert.setThreshold(100.0);
        alert.setStockSymbol("MSFT");
        alert.setDirection(Direction.ABOVE);  // currentPrice < threshold
        alert.setBuyPrice(95.0);

        when(alertRepository.findAll()).thenReturn(List.of(alert));

        PriceFetcherServiceImpl spyService = spy(new PriceFetcherServiceImpl(alertRepository));

        Field alertServiceField = PriceFetcherServiceImpl.class.getDeclaredField("alertService");
        alertServiceField.setAccessible(true);
        alertServiceField.set(spyService, alertService);

        doReturn(90.0).when(spyService).getLatestPrice("MSFT");

        spyService.fetchPrices();

        verify(alertService, never()).evaluateAlerts(any(), anyDouble(), anyString(), anyDouble());
    }
}
