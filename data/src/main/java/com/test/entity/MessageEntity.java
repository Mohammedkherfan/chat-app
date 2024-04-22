package com.test.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class MessageEntity {

    @Id
    private Long id;
    private String messageExternalId;
    private String chatId;
    private String senderId;
    private String receiverId;
    private String body;
    private LocalDateTime dateTime;
}
