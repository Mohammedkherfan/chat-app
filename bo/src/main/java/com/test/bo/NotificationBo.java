package com.test.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationBo {

    private String id;
    private String senderId;
    private String receiverId;
    private String body;
}
