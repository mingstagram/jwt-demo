package com.example.jwt_demo.repository;

import com.example.jwt_demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<com.example.jwt_demo.domain.User, Long> {
    Optional<User> findByUsername(String username);
}