package com.capgemini.Stock.Portfolio.Monitoring.App.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.capgemini.Stock.Portfolio.Monitoring.App.dto.PortfolioSellDto;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.Holding;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.Portfolio;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.User;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.HoldingsRepository;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.PortfolioRepository;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.UserRepository;
import com.capgemini.Stock.Portfolio.Monitoring.App.service.PortfolioServiceImpl;
import com.capgemini.Stock.Portfolio.Monitoring.App.service.PriceFetcherService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

public class PortfolioServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private HoldingsRepository holdingRepository;

    @Mock
    private PriceFetcherService priceFetcherService;

    @InjectMocks
    private PortfolioServiceImpl portfolioService;

    private User user;
    private Portfolio portfolio;
    private Holding holding1, holding2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Setup dummy user, portfolio and holdings
        user = new User();
        portfolio = new Portfolio();
        user.setPortfolio(portfolio);

        holding1 = new Holding();
        holding1.setPortfolio(portfolio);
        holding1.setSymbol("AAPL");
        holding1.setQuantity(10);
        holding1.setBuyPrice(100.0);

        holding2 = new Holding();
        holding2.setPortfolio(portfolio);
        holding2.setSymbol("GOOGL");
        holding2.setQuantity(5);
        holding2.setBuyPrice(1500.0);
    }

    @Test
    public void testGetHoldings() throws Exception {
        when(userRepository.findByEmail("testuser@example.com")).thenReturn(Optional.of(user));
        when(holdingRepository.findByPortfolio(portfolio)).thenReturn(Arrays.asList(holding1, holding2));

        // Stub the priceFetcherService with doReturn to avoid checked exception errors
        doReturn(110.0).when(priceFetcherService).getLatestPrice("AAPL");
        doReturn(1550.0).when(priceFetcherService).getLatestPrice("GOOGL");

        List<Map<String, Object>> holdings = portfolioService.getHoldings("testuser@example.com");

        assertEquals(2, holdings.size());

        Map<String, Object> appleHolding = holdings.get(0);
        assertEquals("AAPL", appleHolding.get("symbol"));
        assertEquals(10, appleHolding.get("quantity"));
        assertEquals(100.0, appleHolding.get("buyPrice"));
        assertEquals(110.0, appleHolding.get("currentPrice"));
        assertEquals(100.0, ((Double) appleHolding.get("gain")));  // (110-100)*10=100
        assertEquals(10.0, ((Double) appleHolding.get("gainPercent"))); // 100/1000 * 100

        Map<String, Object> googleHolding = holdings.get(1);
        assertEquals("GOOGL", googleHolding.get("symbol"));
        assertEquals(5, googleHolding.get("quantity"));
        assertEquals(1500.0, googleHolding.get("buyPrice"));
        assertEquals(1550.0, googleHolding.get("currentPrice"));
        assertEquals(250.0, ((Double) googleHolding.get("gain")));  // (1550-1500)*5=250
        assertEquals(3.3333333333333335, ((Double) googleHolding.get("gainPercent"))); // 250/7500 * 100
    }
    
    @Test
    public void testSellStock_NotEnoughQuantity() {
        when(userRepository.findByEmail("testuser@example.com")).thenReturn(Optional.of(user));
        Holding holding = new Holding();
        holding.setPortfolio(portfolio);
        holding.setSymbol("AAPL");
        holding.setQuantity(5);
        holding.setBuyPrice(100.0);

        when(holdingRepository.findByPortfolioAndSymbol(portfolio, "AAPL")).thenReturn(Optional.of(holding));
        PortfolioSellDto portfolioSellDto = new PortfolioSellDto();
        portfolioSellDto.setUsername("testuser@example.com");
        portfolioSellDto.setQuantity(5);
        portfolioSellDto.setSymbol("AAPL");
        String result = portfolioService.sellStock(portfolioSellDto);

        assertEquals("Not enough quantity to sell.", result);
        verify(holdingRepository, never()).save(any());
        verify(holdingRepository, never()).delete(any());
    }
    
    @Test
    public void testSellStock_SellAllDeletesHolding() {
        when(userRepository.findByEmail("testuser@example.com")).thenReturn(Optional.of(user));
        Holding holding = new Holding();
        holding.setPortfolio(portfolio);
        holding.setSymbol("AAPL");
        holding.setQuantity(5);
        holding.setBuyPrice(100.0);

        when(holdingRepository.findByPortfolioAndSymbol(portfolio, "AAPL")).thenReturn(Optional.of(holding));
        PortfolioSellDto portfolioSellDto = new PortfolioSellDto();
        portfolioSellDto.setUsername("testuser@example.com");
        portfolioSellDto.setQuantity(5);
        portfolioSellDto.setSymbol("AAPL");
        String result = portfolioService.sellStock(portfolioSellDto);

        assertEquals("Stock sold successfully.", result);
        verify(holdingRepository).delete(holding);
    }
}


