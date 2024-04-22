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

    private Long id;
    private String roomExternalId;
    private String chatId;
    private String senderId;
    private String receiverId;
}
