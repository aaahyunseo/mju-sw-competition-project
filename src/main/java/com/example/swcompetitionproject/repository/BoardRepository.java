package com.example.swcompetitionproject.repository;

import com.example.swcompetitionproject.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, UUID> {
}
