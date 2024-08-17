package com.example.swcompetitionproject.repository;

import com.example.swcompetitionproject.entity.ChattingRoom;
import com.example.swcompetitionproject.entity.User;
import com.example.swcompetitionproject.entity.UserRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRoomRepository extends JpaRepository<UserRoom, UUID> {
    List<UserRoom> findByUser(User user);

    Optional<UserRoom> findByUserAndChattingRoom(User user, ChattingRoom chattingRoom);

    Boolean existsByUserAndChattingRoom(User user, ChattingRoom chattingRoom);
}
