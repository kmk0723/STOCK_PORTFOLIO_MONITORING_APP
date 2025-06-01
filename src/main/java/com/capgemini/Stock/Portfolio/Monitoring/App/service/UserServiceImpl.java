package com.capgemini.Stock.Portfolio.Monitoring.App.service;

import com.capgemini.Stock.Portfolio.Monitoring.App.dto.HoldingsDto;
import com.capgemini.Stock.Portfolio.Monitoring.App.dto.UserDTO;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.Holding;

import com.capgemini.Stock.Portfolio.Monitoring.App.model.Portfolio;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.User;
import com.capgemini.Stock.Portfolio.Monitoring.App.Exceptions.UserAlreadyExistsException;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
    private final HoldingsRepository holdingsRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    UserServiceImpl(HoldingsRepository holdingsRepository) {
        this.holdingsRepository = holdingsRepository;
    }

    @Override
    public UserDTO register(UserDTO userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists with email: " + userDto.getEmail());
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());

        User savedUser = userRepository.save(user);

        Portfolio portfolio = new Portfolio();
        portfolio.setUser(savedUser);
        Portfolio port = portfolioRepository.save(portfolio);

        UserDTO result = new UserDTO();
        result.setId(savedUser.getId());
        result.setUsername(savedUser.getUsername());
        result.setEmail(savedUser.getEmail());
        result.setRole(savedUser.getRole());
        result.setPortfolioId(port.getId());

        return result;
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


    @Override
    public  List<HoldingsDto> showHoldings(String admin){
    	Optional<User> isAdmin = userRepository.findByEmail(admin);
    	if(isAdmin.get().getRole().equalsIgnoreCase("ADMIN")) {
    		
    		List<Holding> holdings = holdingsRepository.findAll();
    		List<HoldingsDto> allHoldings = new ArrayList<>();
    		holdings.forEach(i -> {
    			HoldingsDto dto = new HoldingsDto();
    			dto.setBuyPrice(i.getBuyPrice());
    			dto.setPortfolio(i.getPortfolio().getId());
    			dto.setQuantity(i.getQuantity());
    			dto.setSymbol(i.getSymbol());
    			allHoldings.add(dto);
    		});
    		return allHoldings;
    	}else {
    		throw new RuntimeException("ONLY ADMINS CAN ACCESS");
    	}
    	
    }
    
    @Override
    public  List<UserDTO> showUsers(String admin){
    	Optional<User> isAdmin = userRepository.findByEmail(admin);
    	if(isAdmin.get().getRole().equalsIgnoreCase("ADMIN")) {
    		
    		List<User> user = userRepository.findAll();
    		List<UserDTO> allUsers = new ArrayList<>();
    		user.forEach(i -> {
    			if(!i.getId().equals(isAdmin.get().getId())) {
    				UserDTO dto = new UserDTO();
        			dto.setEmail(i.getEmail());
        			dto.setId(i.getId());
        			dto.setPortfolioId(i.getPortfolio().getId());
        			dto.setUsername(i.getUsername());
        			dto.setRole(i.getRole());
        			allUsers.add(dto);
    			}
    			
    		});
    			
    		return allUsers;
    	}else {
    		throw new RuntimeException("ONLY ADMINS CAN ACCESS");
    	}
    	
    }
    
    
    
}

