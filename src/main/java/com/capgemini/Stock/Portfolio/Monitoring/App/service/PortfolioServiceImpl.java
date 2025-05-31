package com.capgemini.Stock.Portfolio.Monitoring.App.service;

import com.capgemini.Stock.Portfolio.Monitoring.App.Exceptions.UserNotFoundException;
import com.capgemini.Stock.Portfolio.Monitoring.App.Exceptions.InsufficientQuantityException;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.Holding;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.Portfolio;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.User;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.HoldingsRepository;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.PortfolioRepository;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PortfolioServiceImpl implements PortfolioService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private HoldingsRepository holdingRepository;

    private PriceFetcherService priceFetcherService;

    @Override
    public List<Map<String, Object>> getHoldings(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        Portfolio portfolio = user.getPortfolio();
        List<Holding> holdings = holdingRepository.findByPortfolio(portfolio);

        List<Map<String, Object>> response = new ArrayList<>();
        for (Holding h : holdings) {
            double currentPrice = getCurrentPrice(h.getSymbol());
            double gain = (currentPrice - h.getBuyPrice()) * h.getQuantity();
            double gainPercent = (gain / (h.getBuyPrice() * h.getQuantity())) * 100;

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("symbol", h.getSymbol());
            data.put("quantity", h.getQuantity());
            data.put("buyPrice", h.getBuyPrice());
            data.put("currentPrice", currentPrice);
            data.put("gain", gain);
            data.put("gainPercent", gainPercent);
            response.add(data);
        }

        return response;
    }

    @Override
    public String buyStock(String username, String symbol, int quantity, double buyPrice) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        Portfolio portfolio = user.getPortfolio();

        Optional<Holding> optional = holdingRepository.findByPortfolioAndSymbol(portfolio, symbol);
        if (optional.isPresent()) {
            Holding h = optional.get();
            int totalQty = h.getQuantity() + quantity;
            h.setBuyPrice(((h.getBuyPrice() * h.getQuantity()) + (buyPrice * quantity)) / totalQty);
            h.setQuantity(totalQty);
            holdingRepository.save(h);
        } else {
            Holding h = new Holding();
            h.setPortfolio(portfolio);
            h.setSymbol(symbol);
            h.setQuantity(quantity);
            h.setBuyPrice(buyPrice);
            holdingRepository.save(h);
        }

        return "Stock bought successfully.";
    }

    @Override
    public String sellStock(String username, String symbol, int quantity) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        Portfolio portfolio = user.getPortfolio();

        Holding h = holdingRepository.findByPortfolioAndSymbol(portfolio, symbol)
                .orElseThrow(() -> new InsufficientQuantityException("Stock not found: " + symbol));

        if (quantity > h.getQuantity()) {
            throw new InsufficientQuantityException("Not enough quantity to sell.");
        }

        h.setQuantity(h.getQuantity() - quantity);
        if (h.getQuantity() == 0) {
            holdingRepository.delete(h);
        } else {
            holdingRepository.save(h);
        }

        return "Stock sold successfully.";
    }

    private double getCurrentPrice(String symbol) {
        try {
            return priceFetcherService.getLatestPrice(symbol);
        } catch (Exception e) {
            return 0;
        }
    }
}
