package com.example.swcompetitionproject.dto.request.board;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomIdDto {
    @NotNull(message = "roomId를 입력해주세요.")
    private UUID roomId;
}