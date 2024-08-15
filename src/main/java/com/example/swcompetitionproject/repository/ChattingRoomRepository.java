package com.example.swcompetitionproject.repository;

import com.example.swcompetitionproject.entity.Board;
import com.example.swcompetitionproject.entity.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, UUID> {
    Optional<ChattingRoom> deleteByBoard(Board board);
}
