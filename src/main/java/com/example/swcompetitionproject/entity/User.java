package com.example.swcompetitionproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table(name = "users")
public class User extends BaseEntity {
    //이름
    private String name;

    //전공(소속)
    private String major;

    //학번
    @Column(nullable = false)
    private String studentNumber;

    //성별
    @Enumerated(EnumType.STRING)
    private GenderType gender;

    //카테고리
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> category;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRoom> userRooms;

    public User setName(String name){
        this.name = name;
        return this;
    }
    public User setGender(GenderType gender){
        this.gender = gender;
        return this;
    }
}
