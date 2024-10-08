package com.example.swcompetitionproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table(name = "boards")
public class Board extends BaseEntity {
    //게시글 제목
    @Column(nullable = false)
    private String title;

    //게시글 본문
    @Column(nullable = false)
    private String content;

    //기숙사 건물 이름
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DormitoryType dormitory;

    //n인실
    @Column(nullable = false)
    private int total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private ChattingRoom chattingRoom;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter
    private List<BoardCategory> boardCategories;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Interest> interests;

    public Board setContent(String content){
        this.content = content;
        return this;
    }
}
