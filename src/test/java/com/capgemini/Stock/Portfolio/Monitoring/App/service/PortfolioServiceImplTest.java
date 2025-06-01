package com.capgemini.Stock.Portfolio.Monitoring.App.service;

import com.capgemini.Stock.Portfolio.Monitoring.App.Exceptions.InsufficientQuantityException;
import com.capgemini.Stock.Portfolio.Monitoring.App.Exceptions.UserNotFoundException;
import com.capgemini.Stock.Portfolio.Monitoring.App.dto.PortfolioDto;
import com.capgemini.Stock.Portfolio.Monitoring.App.dto.PortfolioSellDto;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.*;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PortfolioServiceImplTest {

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
    private Holding holding;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        portfolio = new Portfolio();
        portfolio.setUser(user);

        holding = new Holding();
        holding.setSymbol("AAPL");
        holding.setQuantity(10);
        holding.setBuyPrice(100.0);
        holding.setPortfolio(portfolio);

        user.setPortfolio(portfolio);
    }

    @Test
    void testGetHoldings_success() throws Exception {
        when(userRepository.findByEmail("testuser")).thenReturn(Optional.of(user));
        when(holdingRepository.findByPortfolio(portfolio)).thenReturn(List.of(holding));
        when(priceFetcherService.getLatestPrice("AAPL")).thenReturn(120.0);

        List<Map<String, Object>> result = portfolioService.getHoldings("testuser");

        assertEquals(1, result.size());
        assertEquals("AAPL", result.get(0).get("symbol"));
        assertEquals(120.0, result.get(0).get("currentPrice"));
    }

    @Test
    void testGetHoldings_userNotFound() {
        when(userRepository.findByEmail("unknownuser")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            portfolioService.getHoldings("unknownuser");
        });
    }

    @Test
    void testBuyStock_newStock() throws Exception {
        PortfolioDto dto = new PortfolioDto();
        dto.setUsername("testuser");
        dto.setSymbol("GOOG");
        dto.setQuantity(5);

        when(userRepository.findByEmail("testuser")).thenReturn(Optional.of(user));
        when(holdingRepository.findByPortfolioAndSymbol(portfolio, "GOOG")).thenReturn(Optional.empty());
        when(priceFetcherService.getLatestPrice("GOOG")).thenReturn(150.0);

        String result = portfolioService.buyStock(dto);

        assertEquals("Stock bought successfully.", result);
        verify(holdingRepository, times(1)).save(any(Holding.class));
    }

    @Test
    void testBuyStock_existingStock() throws Exception {
        PortfolioDto dto = new PortfolioDto();
        dto.setUsername("testuser");
        dto.setSymbol("AAPL");
        dto.setQuantity(10);

        when(userRepository.findByEmail("testuser")).thenReturn(Optional.of(user));
        when(holdingRepository.findByPortfolioAndSymbol(portfolio, "AAPL")).thenReturn(Optional.of(holding));
        when(priceFetcherService.getLatestPrice("AAPL")).thenReturn(110.0);

        String result = portfolioService.buyStock(dto);

        assertEquals("Stock bought successfully.", result);
        assertEquals(20, holding.getQuantity());
        verify(holdingRepository, times(1)).save(holding);
    }

    @Test
    void testSellStock_success() {
        PortfolioSellDto dto = new PortfolioSellDto();
        dto.setUsername("testuser");
        dto.setSymbol("AAPL");
        dto.setQuantity(5);

        when(userRepository.findByEmail("testuser")).thenReturn(Optional.of(user));
        when(holdingRepository.findByPortfolioAndSymbol(portfolio, "AAPL")).thenReturn(Optional.of(holding));

        String result = portfolioService.sellStock(dto);

        assertEquals("Stock sold successfully.", result);
        assertEquals(5, holding.getQuantity());
        verify(holdingRepository).save(holding);
    }

    @Test
    void testSellStock_insufficientQuantity() {
        PortfolioSellDto dto = new PortfolioSellDto();
        dto.setUsername("testuser");
        dto.setSymbol("AAPL");
        dto.setQuantity(15); // More than available

        when(userRepository.findByEmail("testuser")).thenReturn(Optional.of(user));
        when(holdingRepository.findByPortfolioAndSymbol(portfolio, "AAPL")).thenReturn(Optional.of(holding));

        assertThrows(InsufficientQuantityException.class, () -> {
            portfolioService.sellStock(dto);
        });
    }

    @Test
    void testSellStock_notFound() {
        PortfolioSellDto dto = new PortfolioSellDto();
        dto.setUsername("testuser");
        dto.setSymbol("MSFT");
        dto.setQuantity(1);

        when(userRepository.findByEmail("testuser")).thenReturn(Optional.of(user));
        when(holdingRepository.findByPortfolioAndSymbol(portfolio, "MSFT")).thenReturn(Optional.empty());

        assertThrows(InsufficientQuantityException.class, () -> {
            portfolioService.sellStock(dto);
        });
    }
}
