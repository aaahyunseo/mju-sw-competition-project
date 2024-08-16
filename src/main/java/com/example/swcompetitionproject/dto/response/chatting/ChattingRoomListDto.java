package com.example.swcompetitionproject.dto.response.chatting;

import com.example.swcompetitionproject.entity.ChattingRoom;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ChattingRoomListDto {
    private UUID id;
    private String title;
    private int memberCount;

    public static ChattingRoomListDto from(ChattingRoom room) {
        return ChattingRoomListDto.builder()
                .id(room.getId())
                .title(room.getTitle())
                .memberCount(room.getMemberCount())
                .build();
    }
}
