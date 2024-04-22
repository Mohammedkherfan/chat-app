package com.test.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetRoomResponse {

    private String roomExternalId;
    private String chatId;
    private String senderId;
    private String receiverId;
}
