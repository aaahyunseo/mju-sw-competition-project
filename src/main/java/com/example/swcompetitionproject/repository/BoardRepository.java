package com.example.swcompetitionproject.repository;

import com.example.swcompetitionproject.entity.Board;
import com.example.swcompetitionproject.entity.ChattingRoom;
import com.example.swcompetitionproject.entity.DormitoryType;
import com.example.swcompetitionproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, UUID> {
    List<Board> findAllByDormitoryOrderByCreatedAtDesc(DormitoryType dormitory);

    Optional<Board> findBoardByDormitoryAndId(DormitoryType dormitory, UUID id);

    Optional<Board> findBoardByDormitoryAndIdAndUser(DormitoryType dormitory, UUID id, User user);

    List<Board> findAllByUserOrderByCreatedAtDesc(User user);

    int countByDormitory(DormitoryType dormitoryType);

    Optional<Board> findBoardByChattingRoom(ChattingRoom chattingRoom);

    List<Board> findAllByUser(User user);
}
