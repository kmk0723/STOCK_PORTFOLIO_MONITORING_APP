package com.capgemini.Stock.Portfolio.Monitoring.App.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.capgemini.Stock.Portfolio.Monitoring.App.model.User;
import com.capgemini.Stock.Portfolio.Monitoring.App.repository.UserRepository;

@Service
public class UserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;

    // Constructor for dependency injection
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
    }

    // Register User: check if username exists, encrypt password, set role and save user
    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        
        // Encrypt the password before saving
//        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set default role if not provided
        if (user.getRole() == null) {
            user.setRole(User.Role.USER);
        }
        return userRepository.save(user);
    }
    
    public List<User> getAllUsers(){
    	return userRepository.findAll();
    }

    // Find user by username
    public User findByUsername(String username) {
    	User user = userRepository.findByUsername(username);
    	if(user.getRole() == User.Role.ADMIN) {
    		return user;
    	}else {
            throw new UsernameNotFoundException(" NO Access: " + username);
    	}
    }
    
    public String login(String username,String password) {
        User user = userRepository.findByUsername(username);
        
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        
        if(user.getPassword().matches(password)) {
        	return "Login suucees";
        }else {
            throw new UsernameNotFoundException(" Wrong password : " + username);

        }
    }
    
    

    // Spring Security's method for loading a user by username
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username);
//        
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found with username: " + username);
//        }
//        
//        // Return Spring Security's UserDetails object with the correct authorities
//        return new org.springframework.security.core.userdetails.User(
//            user.getUsername(),
//            user.getPassword(),
//            getAuthorities(user.getRole())
//        );
//    }

//     Get authorities (roles) for Spring Security
    private Collection<? extends GrantedAuthority> getAuthorities(User.Role role) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
    
    
    
    // Custom exception for username already exists (you can define this class separately)
    public static class UsernameAlreadyExistsException extends RuntimeException {
        public UsernameAlreadyExistsException(String message) {
            super(message);
        }
    }
}
