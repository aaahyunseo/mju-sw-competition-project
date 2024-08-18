package com.example.swcompetitionproject.dto.request.chatting;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChatMessageDto {
    @NotBlank(message = "roomId를 입력해주세요.")
    private UUID roomId;
    private String content;
    @NotBlank(message = "메세지 발신자를 입력해주세요.")
    private String sender;
    private LocalDateTime timestamp;
}
