package com.example.swcompetitionproject.dto.request.chatting;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ChatRequestDto {
    @NotBlank(message = "메세지 내용을 입력해주세요.")
    private String content;

    @NotNull(message = "채팅방 아이디가 누락되었습니다.")
    private UUID roomId;
}
