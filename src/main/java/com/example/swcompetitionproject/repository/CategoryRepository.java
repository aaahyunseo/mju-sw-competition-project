package com.example.swcompetitionproject.repository;

import com.example.swcompetitionproject.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
