package com.test.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageBo {

    private Long id;
    private String messageExternalId;
    private String chatId;
    private String senderId;
    private String receiverId;
    private String body;
    private LocalDateTime dateTime;
}
