package com.example.swcompetitionproject.dto.request.chatting;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ChatMessageDto {
    @NotBlank(message = "roomId를 입력해주세요.")
    private UUID roomId;
    @NotBlank(message = "메세지 내용을 입력해주세요.")
    private String content;
    @NotBlank(message = "메세지 발신자를 입력해주세요.")
    private String sender;
}
