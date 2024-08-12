package com.example.swcompetitionproject.repository;

import com.example.swcompetitionproject.entity.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoomMemberRepository extends JpaRepository<RoomMember, UUID> {
}
