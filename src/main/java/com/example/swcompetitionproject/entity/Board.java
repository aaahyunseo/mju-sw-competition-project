package com.example.swcompetitionproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table(name = "boards")
public class Board extends BaseEntity {
    //제목
    @Column(nullable = false)
    private String title;

    //카테고리
    @ElementCollection
    @CollectionTable(name = "board_categories", joinColumns = @JoinColumn(name = "board_id"))
    @Column(name = "category", nullable = false)
    private List<String> category;

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

    public Board setContent(String content){
        this.content = content;
        return this;
    }
    public Board setCategory(List<String> category){
        this.category = category;
        return this;
    }
}
