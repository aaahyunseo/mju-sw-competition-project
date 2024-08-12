package com.example.swcompetitionproject.repository;

import com.example.swcompetitionproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}