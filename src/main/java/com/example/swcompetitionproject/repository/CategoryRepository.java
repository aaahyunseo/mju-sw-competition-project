package com.example.swcompetitionproject.repository;

import com.example.swcompetitionproject.entity.Category;
import com.example.swcompetitionproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findAllByUserOrderByCreatedAtAsc(User user);

    Optional<Category> findByUserAndId(User user, UUID id);
}
