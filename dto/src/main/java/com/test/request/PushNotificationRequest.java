package com.test.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NotNull(message = "Invalid request")
public class PushNotificationRequest {

    private String messageExternalId;
    private String senderId;
    private String receiverId;
    private String body;
}
