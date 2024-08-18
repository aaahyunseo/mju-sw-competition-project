package com.example.swcompetitionproject.repository;

import com.example.swcompetitionproject.entity.Board;
import com.example.swcompetitionproject.entity.Interest;
import com.example.swcompetitionproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InterestRepository extends JpaRepository<Interest, UUID> {
    boolean existsByUserAndBoard(User user, Board board);
    Optional<Interest> findByUserAndBoard(User user, Board board);
    @Query("SELECT i.board FROM Interest i WHERE i.user = :user")
    List<Board> findBoardsByUser(@Param("user") User user);
}
