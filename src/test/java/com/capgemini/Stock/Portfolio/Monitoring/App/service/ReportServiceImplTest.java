package com.capgemini.Stock.Portfolio.Monitoring.App.service;

import com.capgemini.Stock.Portfolio.Monitoring.App.model.Holding;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.Portfolio;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.User;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.HoldingsRepository;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReportServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HoldingsRepository holdingRepository;

    @Mock
    private PriceFetcherServiceImpl priceFetcherService;

    @InjectMocks
    private ReportServiceImpl reportService;

    private User user;
    private Portfolio portfolio;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Setup User and Portfolio mocks
        portfolio = new Portfolio();
        user = new User();
        user.setPortfolio(portfolio);
    }

    private Holding createHolding(String symbol, int quantity, double buyPrice) {
        Holding h = new Holding();
        h.setSymbol(symbol);
        h.setQuantity(quantity);
        h.setBuyPrice(buyPrice);
        return h;
    }

    @Test
    public void testGetPortfolioSummary_ReturnsHoldingsList() {
        // Prepare test holdings
        Holding h1 = createHolding("AAPL", 10, 150.0);
        Holding h2 = createHolding("GOOG", 5, 2000.0);

        List<Holding> holdings = List.of(h1, h2);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(holdingRepository.findByPortfolio(portfolio)).thenReturn(holdings);

        List<Holding> result = reportService.getPortfolioSummary("test@example.com");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("AAPL", result.get(0).getSymbol());
        assertEquals("GOOG", result.get(1).getSymbol());

        verify(userRepository).findByEmail("test@example.com");
        verify(holdingRepository).findByPortfolio(portfolio);
    }

    @Test
    public void testExportToExcel_ReturnsExcelStream() throws Exception {
        Holding h1 = createHolding("AAPL", 10, 150.0);
        Holding h2 = createHolding("GOOG", 5, 2000.0);

        List<Holding> holdings = List.of(h1, h2);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(holdingRepository.findByPortfolio(portfolio)).thenReturn(holdings);

        // Mock price fetcher to return current prices
        when(priceFetcherService.getLatestPrice("AAPL")).thenReturn(160.0);
        when(priceFetcherService.getLatestPrice("GOOG")).thenReturn(2100.0);

        ByteArrayInputStream excelStream = reportService.exportToExcel("test@example.com");

        assertNotNull(excelStream);

        verify(userRepository).findByEmail("test@example.com");
        verify(holdingRepository).findByPortfolio(portfolio);
        verify(priceFetcherService, times(2)).getLatestPrice(anyString());
    }
}


