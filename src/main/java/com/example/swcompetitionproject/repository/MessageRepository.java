package com.example.swcompetitionproject.repository;

import com.example.swcompetitionproject.entity.ChattingRoom;
import com.example.swcompetitionproject.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByChattingRoomOrderByCreatedAtAsc(ChattingRoom room);
}
