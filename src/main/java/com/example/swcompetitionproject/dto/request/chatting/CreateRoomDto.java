package com.example.swcompetitionproject.dto.request.chatting;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoomDto {
    @NotBlank(message = "방 제목을 입력해주세요.")
    private String title;
}
