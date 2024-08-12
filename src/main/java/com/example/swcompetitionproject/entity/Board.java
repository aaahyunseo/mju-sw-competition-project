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
@Table(name = "boards")
public class Board extends BaseEntity {
    //제목
    @Column(nullable = false)
    private String title;

    //카테고리
    @Column(nullable = false)
    private String category;

    //본문
    @Column(nullable = false)
    private String content;

    //기숙사 건물 이름
    @Column(nullable = false)
    private String dormitory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    @JoinColumn(name="chattingRoom_id")
    private ChattingRoom chattingRoom;
}
