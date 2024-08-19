package com.example.swcompetitionproject.repository;

import com.example.swcompetitionproject.entity.BoardCategory;
import com.example.swcompetitionproject.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory, UUID> {
    List<BoardCategory> findAllByCategory(Category category);
}
