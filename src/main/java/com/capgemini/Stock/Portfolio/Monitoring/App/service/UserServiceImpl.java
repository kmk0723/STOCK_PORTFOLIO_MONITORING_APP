package com.capgemini.Stock.Portfolio.Monitoring.App.service;
import com.capgemini.Stock.Portfolio.Monitoring.App.dto.UserDTO;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.Portfolio;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.User;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.PortfolioRepository;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Override
    public UserDTO register(UserDTO userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());

        User savedUser = userRepository.save(user);

        Portfolio portfolio = new Portfolio();
        portfolio.setUser(savedUser);
        portfolioRepository.save(portfolio);

        // Mapping to DTO
        UserDTO result = new UserDTO();
        result.setId(savedUser.getId());
        result.setUsername(savedUser.getUsername());
        result.setEmail(savedUser.getEmail());
        result.setRole(savedUser.getRole());
        result.setPortfolioId(null);

        return userDto;
    }

    @Override
    public Optional<UserDTO> login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(u -> u.getPassword().equals(password))
                .map(user -> {
                	UserDTO dto = new UserDTO();
                    dto.setUsername(user.getUsername());
                    dto.setEmail(user.getEmail());
                    dto.setRole(user.getRole());
                    dto.setPortfolioId(user.getPortfolio().getId());
                    return dto;
                });
    }
}