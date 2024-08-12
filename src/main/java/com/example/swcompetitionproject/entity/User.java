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
@Table(name = "users")
public class User extends BaseEntity{
    //이름
    @Column(nullable = false)
    private String name;

    //전공(소속)
    @Column(nullable = false)
    private String major;

    //구분
    @Column(nullable = false)
    private String division;

    //학년
    @Column(nullable = false)
    private int grade;

    //학번
    @Column(nullable = false)
    private String student_number;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards;
}
