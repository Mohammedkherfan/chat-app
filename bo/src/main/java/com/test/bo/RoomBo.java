package com.test.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomBo {

    private String id;
    private String chatId;
    private String senderId;
    private String recipientId;
}
