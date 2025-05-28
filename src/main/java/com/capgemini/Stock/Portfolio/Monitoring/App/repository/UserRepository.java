package com.capgemini.Stock.Portfolio.Monitoring.App.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.Stock.Portfolio.Monitoring.App.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    boolean existsByUsername(String username);
}
