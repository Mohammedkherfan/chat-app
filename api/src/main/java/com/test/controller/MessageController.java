package com.test.controller;

import com.test.bo.NotificationBo;
import com.test.dto.MessageDto;
import com.test.request.PushMessageRequest;
import com.test.response.PushMessageResponse;
import com.test.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public MessageController(MessageService messageService, SimpMessagingTemplate simpMessagingTemplate) {
        this.messageService = messageService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    /**
     * Get API to list all messages between sender and recipient
     * @param senderId
     * @param recipientId
     * @return
     */
    @GetMapping("/messages")
    public ResponseEntity<List<MessageDto>> list(@RequestParam("senderId") String senderId,
                                                 @RequestParam("recipientId") String recipientId) {
        return ResponseEntity.ok(messageService.list(senderId, recipientId).getMessages());
    }

    /**
     * Socket method to send message
     * @param request
     */
    @MessageMapping("/chat")
    public void push(@Payload PushMessageRequest request) {
        PushMessageResponse pushMessageResponse = messageService.push(request);
        simpMessagingTemplate.convertAndSendToUser(
                request.getRecipientId(), "/queue/messages",
                new NotificationBo(
                        pushMessageResponse.getId(),
                        pushMessageResponse.getSenderId(),
                        pushMessageResponse.getRecipientId(),
                        pushMessageResponse.getContent()
                )
        );
    }
}
