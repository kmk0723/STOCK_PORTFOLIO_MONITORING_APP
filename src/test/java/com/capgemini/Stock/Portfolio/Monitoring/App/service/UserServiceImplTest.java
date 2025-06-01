package com.capgemini.Stock.Portfolio.Monitoring.App.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import com.capgemini.Stock.Portfolio.Monitoring.App.model.*;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.*;
import com.capgemini.Stock.Portfolio.Monitoring.App.service.UserServiceImpl;
import com.capgemini.Stock.Portfolio.Monitoring.App.dto.UserDTO;

public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private HoldingsRepository holdingsRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Manually inject mocks into userService fields since it uses @Autowired field injection
        // This step is often optional if you use @InjectMocks, but helps to be explicit
        // (can also use ReflectionTestUtils if needed)
        ReflectionTestUtils.setField(userService, "userRepository", userRepository);
        ReflectionTestUtils.setField(userService, "portfolioRepository", portfolioRepository);
        ReflectionTestUtils.setField(userService, "holdingsRepository", holdingsRepository);
    }

    @Test
    public void testRegisterUser_Success() {
        UserDTO userDto = new UserDTO();
        userDto.setUsername("testuser");
        userDto.setEmail("test@example.com");
        userDto.setPassword("password");
        userDto.setRole("USER");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setEmail("test@example.com");
        savedUser.setPassword("password");
        savedUser.setRole("USER");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        Portfolio portfolio = new Portfolio();
        portfolio.setId(10L);
        portfolio.setUser(savedUser);

        when(portfolioRepository.save(any(Portfolio.class))).thenReturn(portfolio);

        UserDTO result = userService.register(userDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("USER", result.getRole());
        assertEquals(10L, result.getPortfolioId());
    }

    @Test
    public void testLogin_Success() {
        String email = "test@example.com";
        String password = "password";

        User user = new User();
        user.setUsername("testuser");
        user.setEmail(email);
        user.setPassword(password);
        user.setRole("USER");

        Portfolio portfolio = new Portfolio();
        portfolio.setId(20L);
        portfolio.setUser(user);
        user.setPortfolio(portfolio);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<UserDTO> result = userService.login(email, password);

        assertTrue(result.isPresent());
        UserDTO dto = result.get();
        assertEquals("testuser", dto.getUsername());
        assertEquals(email, dto.getEmail());
        assertEquals("USER", dto.getRole());
        assertEquals(20L, dto.getPortfolioId());
    }

    @Test
    public void testLogin_Fail_WrongPassword() {
        String email = "test@example.com";
        String password = "password";
        String wrongPassword = "wrong";

        User user = new User();
        user.setUsername("testuser");
        user.setEmail(email);
        user.setPassword(password);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<UserDTO> result = userService.login(email, wrongPassword);

        assertFalse(result.isPresent());
    }

    @Test
    public void testShowHoldings_AsAdmin_Success() {
        String adminEmail = "admin@example.com";

        User adminUser = new User();
        adminUser.setId(1L);
        adminUser.setEmail(adminEmail);
        adminUser.setRole("ADMIN");

        Portfolio portfolio = new Portfolio();
        portfolio.setId(100L);
        portfolio.setUser(adminUser);
        adminUser.setPortfolio(portfolio);

        Holding holding1 = new Holding();
        holding1.setBuyPrice(50.0);
        holding1.setPortfolio(portfolio);
        holding1.setQuantity(10);
        holding1.setSymbol("AAPL");

        Holding holding2 = new Holding();
        holding2.setBuyPrice(100.0);
        holding2.setPortfolio(portfolio);
        holding2.setQuantity(20);
        holding2.setSymbol("GOOG");

        List<Holding> holdingsList = Arrays.asList(holding1, holding2);

        when(userRepository.findByEmail(adminEmail)).thenReturn(Optional.of(adminUser));
        when(holdingsRepository.findAll()).thenReturn(holdingsList);

        List<?> result = userService.showHoldings(adminEmail);

        assertEquals(2, result.size());
    }

    @Test
    public void testShowHoldings_AsNonAdmin_Throws() {
        String userEmail = "user@example.com";

        User normalUser = new User();
        normalUser.setId(2L);
        normalUser.setEmail(userEmail);
        normalUser.setRole("USER");

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(normalUser));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.showHoldings(userEmail);
        });

        assertEquals("ONLY ADMINS CAN ACCESS", exception.getMessage());
    }

    // Add similar tests for showUsers and other methods...

}
