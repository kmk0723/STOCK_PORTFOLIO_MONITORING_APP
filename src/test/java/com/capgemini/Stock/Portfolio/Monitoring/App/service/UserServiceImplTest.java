package com.capgemini.Stock.Portfolio.Monitoring.App.service;

import com.capgemini.Stock.Portfolio.Monitoring.App.dto.UserDTO;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.Portfolio;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.User;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.PortfolioRepository;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PortfolioRepository portfolioRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testRegister() {
        UserDTO userDto = new UserDTO();
        userDto.setUsername("testuser");
        userDto.setEmail("test@example.com");
        userDto.setPassword("password");
        userDto.setRole("USER");

        User userEntity = new User();
        userEntity.setId(1L);
        userEntity.setUsername(userDto.getUsername());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setPassword(userDto.getPassword());
        userEntity.setRole(userDto.getRole());

        when(userRepository.save(any(User.class))).thenReturn(userEntity);

        UserDTO result = userService.register(userDto);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("USER", result.getRole());

        verify(userRepository, times(1)).save(any(User.class));
        verify(portfolioRepository, times(1)).save(any(Portfolio.class));
    }

    @Test
    public void testLoginSuccess() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        Portfolio portfolio = new Portfolio();
        portfolio.setId(10L);
        user.setPortfolio(portfolio);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        Optional<UserDTO> result = userService.login("test@example.com", "password");

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
        assertEquals(10L, result.get().getPortfolioId());
    }

    @Test
    public void testLoginFailure() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        Optional<UserDTO> result = userService.login("test@example.com", "wrongpassword");

        assertFalse(result.isPresent());
    }
}

