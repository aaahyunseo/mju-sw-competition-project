package com.example.swcompetitionproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chatting_rooms")
public class ChattingRoom extends BaseEntity {
    //방이름
    @Column(nullable = false)
    private String title;

    //방장
    @Column(nullable = false)
    private String manager;

    //현재 채팅방에 들어있는 인원 수
    @Column(nullable = false)
    private int member_count;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_id")
    private Board board;

    @OneToMany(mappedBy = "chattingRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomMember> roomMembers;

    @OneToMany(mappedBy = "chattingRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;
}
