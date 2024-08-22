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
    @NotBlank(message = "메세지 발신자를 입력해주세요.")
    private String sender;
    @NotBlank(message = "userId를 입력해주세요.")
    private UUID userId;
    private String content;
    private LocalDateTime timestamp;
}
