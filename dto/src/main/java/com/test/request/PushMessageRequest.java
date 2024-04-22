package com.test.request;

import jakarta.validation.constraints.NotBlank;
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
public class PushMessageRequest {

    @NotBlank(message = "Invalid sender id")
    private String senderId;
    @NotBlank(message = "Invalid receiver id")
    private String receiverId;
    @NotBlank(message = "Invalid body")
    private String body;
}
