package com.example.swcompetitionproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "messages")
public class Message extends BaseEntity {
    //메세지 내용
    @Column(nullable = false)
    private String content;

    //발신자
    @Column(nullable = false)
    private String sender;

    //발신자 UUID
    @Column(nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chattingRoom_id")
    private ChattingRoom chattingRoom;
}
