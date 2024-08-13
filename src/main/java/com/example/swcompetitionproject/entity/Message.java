package com.example.swcompetitionproject.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chattingRoom_id")
    private ChattingRoom chattingRoom;
}
