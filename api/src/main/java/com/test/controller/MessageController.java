package com.test.controller;

import com.test.request.PushMessageRequest;
import com.test.request.PushNotificationRequest;
import com.test.response.ListMessagesResponse;
import com.test.response.PushMessageResponse;
import com.test.service.MessageService;
import com.test.service.NotificationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MessageController {

    private final MessageService messageService;
    private final NotificationService notificationService;

    @Autowired
    public MessageController(MessageService messageService, NotificationService notificationService) {
        this.messageService = messageService;
        this.notificationService = notificationService;
    }

    @GetMapping("/message")
    public ListMessagesResponse list(@RequestParam("senderId") @Valid @NotBlank(message = "Invalid sender id") String senderId,
                                     @RequestParam("receiverId") @Valid @NotBlank(message = "Invalid receiver id") String receiverId) {
        return messageService.list(senderId, receiverId);
    }

    @MessageMapping("/message")
    public PushMessageResponse push(@Payload @Valid PushMessageRequest request) {
        PushMessageResponse pushMessageResponse = messageService.push(request);
        notificationService.notifyReceiver(PushNotificationRequest.builder()
                .senderId(pushMessageResponse.getSenderId())
                .receiverId(pushMessageResponse.getReceiverId())
                .body(pushMessageResponse.getBody())
                .messageExternalId(pushMessageResponse.getMessageExternalId())
                .build());
        return pushMessageResponse;
    }
}
