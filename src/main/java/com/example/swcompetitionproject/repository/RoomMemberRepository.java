package com.example.swcompetitionproject.repository;

import com.example.swcompetitionproject.entity.ChattingRoom;
import com.example.swcompetitionproject.entity.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoomMemberRepository extends JpaRepository<RoomMember, UUID> {
    Optional<RoomMember> findByNameAndChattingRoom(String name, ChattingRoom chattingRoom);
}
