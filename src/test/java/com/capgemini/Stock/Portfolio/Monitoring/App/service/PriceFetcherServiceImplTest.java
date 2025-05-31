package com.capgemini.Stock.Portfolio.Monitoring.App.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.capgemini.Stock.Portfolio.Monitoring.App.model.Alert;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.Alert.Direction;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.AlertRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PriceFetcherServiceImplTest {

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private AlertService alertService;

    @InjectMocks
    private PriceFetcherServiceImpl priceFetcherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTotalPriceWithQuantity() throws Exception {
        // Spy on priceFetcherService to mock getLatestPrice call
        PriceFetcherServiceImpl spyService = spy(priceFetcherService);

        doReturn(100.0).when(spyService).getLatestPrice("AAPL");

        HashMap<Double, String> result = spyService.getTotalPrice("AAPL", 5);

        assertNotNull(result);
        assertTrue(result.containsKey(500.0)); // 100 * 5 = 500
        assertNotNull(result.get(500.0));
    }

    @Test
    void testGetTotalPriceWithoutQuantity() throws Exception {
        PriceFetcherServiceImpl spyService = spy(priceFetcherService);

        doReturn(150.0).when(spyService).getLatestPrice("GOOG");

        HashMap<Double, String> result = spyService.getTotalPrice("GOOG");

        assertNotNull(result);
        assertTrue(result.containsKey(150.0));
        assertNotNull(result.get(150.0));
    }

    @Test
    void testFetchPricesTriggersAboveAlert() throws Exception {
        Alert alert = new Alert();
        alert.setId(1L);
        alert.setStockSymbol("MSFT");
        alert.setBuyPrice(100.0);
        alert.setThreshold(120.0);
        alert.setDirection(Direction.ABOVE);

        List<Alert> alertList = Arrays.asList(alert);

        when(alertRepository.findAll()).thenReturn(alertList);

        // Spy service to mock getLatestPrice
        PriceFetcherServiceImpl spyService = spy(priceFetcherService);
        doReturn(130.0).when(spyService).getLatestPrice("MSFT");

        // call fetchPrices
        spyService.fetchPrices();

        // verify alertService.evaluateAlerts was called once with expected params
        verify(alertService, times(1))
            .evaluateAlerts(eq(1L),eq(150.0), eq("MSFT"), eq(130.0));
    }

    @Test
    void testFetchPricesTriggersBelowAlert() throws Exception {
        Alert alert = new Alert();
        alert.setId(2L);
        alert.setStockSymbol("TSLA");
        alert.setBuyPrice(200.0);
        alert.setThreshold(180.0);
        alert.setDirection(Direction.BELOW);

        List<Alert> alertList = Arrays.asList(alert);

        when(alertRepository.findAll()).thenReturn(alertList);

        PriceFetcherServiceImpl spyService = spy(priceFetcherService);
        doReturn(170.0).when(spyService).getLatestPrice("TSLA");

        spyService.fetchPrices();

        verify(alertService, times(1))
            .evaluateAlerts(eq(2L),eq(180.0), eq("TSLA"), eq(170.0));
    }

    @Test
    void testFetchPricesDoesNotTriggerWhenConditionsNotMet() throws Exception {
        Alert alert = new Alert();
        alert.setId(3L);
        alert.setStockSymbol("NFLX");
        alert.setBuyPrice(300.0);
        alert.setThreshold(350.0);
        alert.setDirection(Direction.BELOW);

        List<Alert> alertList = Arrays.asList(alert);

        when(alertRepository.findAll()).thenReturn(alertList);

        PriceFetcherServiceImpl spyService = spy(priceFetcherService);
        doReturn(340.0).when(spyService).getLatestPrice("NFLX"); // Below threshold

        spyService.fetchPrices();

        verify(alertService, times(0)).evaluateAlerts(anyLong(),anyDouble(), anyString(), anyDouble());
    }
}



