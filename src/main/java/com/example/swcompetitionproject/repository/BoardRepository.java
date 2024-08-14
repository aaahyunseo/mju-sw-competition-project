package com.example.swcompetitionproject.repository;

import com.example.swcompetitionproject.entity.Board;
import com.example.swcompetitionproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, UUID> {
    List<Board> findAllByDormitory(String dormitory);
    Optional<Board> findBoardByDormitoryAndId(String dormitory, UUID id);
    Optional<Board> findBoardByDormitoryAndIdAndUser(String dormitory, UUID id, User user);
}
