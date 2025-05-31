package com.capgemini.Stock.Portfolio.Monitoring.App.service;

import com.capgemini.Stock.Portfolio.Monitoring.App.dto.HoldingsDto;
import com.capgemini.Stock.Portfolio.Monitoring.App.dto.UserDTO;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.Holding;
import com.capgemini.Stock.Portfolio.Monitoring.App.model.User;

import java.util.List;
import java.util.Optional;
public interface UserService {
    UserDTO register(UserDTO userDto);
    Optional<UserDTO> login(String email, String password);
    List<HoldingsDto> showHoldings(String admin);
    List<UserDTO> showUsers(String admin);
}
