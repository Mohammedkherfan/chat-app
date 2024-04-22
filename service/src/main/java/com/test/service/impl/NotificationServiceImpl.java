package com.test.service.impl;

import com.test.bo.NotificationBo;
import com.test.request.PushNotificationRequest;
import com.test.service.NotificationService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public NotificationServiceImpl(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void notifyReceiver(PushNotificationRequest request) {
        simpMessagingTemplate.convertAndSendToUser(request.getReceiverId(), "/queue/messgae", NotificationBo.builder()
                .id(request.getMessageExternalId())
                .senderId(request.getSenderId())
                .receiverId(request.getReceiverId())
                .body(request.getBody())
                .build());
    }
}
