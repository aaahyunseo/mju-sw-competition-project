package com.example.swcompetitionproject.repository;

import com.example.swcompetitionproject.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByStudentNumber(String studentNumber);

    @EntityGraph(attributePaths = "category")
    Optional<User> findById(UUID id);

    Optional<User> findByName(String name);
}