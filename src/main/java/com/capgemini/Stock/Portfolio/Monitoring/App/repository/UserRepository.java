package com.capgemini.Stock.Portfolio.Monitoring.App.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.Stock.Portfolio.Monitoring.App.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
