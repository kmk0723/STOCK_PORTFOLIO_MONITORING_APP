package com.capgemini.Stock.Portfolio.Monitoring.App.service;

import com.capgemini.Stock.Portfolio.Monitoring.App.dto.UserDTO;
import java.util.Optional;
public interface UserService {
    UserDTO register(UserDTO userDto);
    Optional<UserDTO> login(String email, String password);
}
