package com.example.swcompetitionproject.dto.response.chatting;

import com.example.swcompetitionproject.entity.ChattingRoom;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ChattingRoomListData {
    private List<ChattingRoomListDto> chattingRoomList;

    public static ChattingRoomListData from(List<ChattingRoom> chattingRooms) {
        return ChattingRoomListData.builder()
                .chattingRoomList(
                        chattingRooms.stream()
                                .map(ChattingRoomListDto::from)
                                .collect(Collectors.toList()))
                .build();
    }
}
