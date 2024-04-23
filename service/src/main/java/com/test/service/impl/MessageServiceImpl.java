package com.test.service.impl;

import com.test.bo.MessageBo;
import com.test.bo.RoomBo;
import com.test.dto.MessageDto;
import com.test.exception.ChatAppException;
import com.test.repository.MessageRepository;
import com.test.repository.RoomRepository;
import com.test.request.PushMessageRequest;
import com.test.response.ListMessagesResponse;
import com.test.response.PushMessageResponse;
import com.test.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;

    public MessageServiceImpl(MessageRepository messageRepository, RoomRepository roomRepository) {
        this.messageRepository = messageRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public PushMessageResponse push(PushMessageRequest request) {
        RoomBo roomBo = getRoom(request.getSenderId(), request.getRecipientId());
        MessageBo messageBo = messageRepository.save(MessageBo.builder()
                .id(request.getId())
                .chatId(roomBo.getChatId())
                .senderId(request.getSenderId())
                .recipientId(request.getRecipientId())
                .content(request.getContent())
                .timestamp(request.getTimestamp())
                .build());
        return PushMessageResponse.builder()
                .id(messageBo.getId())
                .chatId(messageBo.getChatId())
                .senderId(messageBo.getSenderId())
                .recipientId(messageBo.getRecipientId())
                .content(messageBo.getContent())
                .timestamp(messageBo.getTimestamp())
                .build();
    }

    @Override
    public ListMessagesResponse list(String senderId, String recipientId) {
        Optional<RoomBo> roomBo = roomRepository.findBySenderIdAndRecipientId(senderId, recipientId);
        if (roomBo.isEmpty())
            throw new ChatAppException("Invalid room");
        List<MessageDto> messages = messageRepository.findAllByChatId(roomBo.get().getChatId()).stream().map(bo -> MessageDto.builder()
                .id(bo.getId())
                .chatId(bo.getChatId())
                .senderId(bo.getSenderId())
                .recipientId(bo.getRecipientId())
                .content(bo.getContent())
                .timestamp(bo.getTimestamp())
                .build())
                .toList();
        return ListMessagesResponse.builder()
                .messages(messages)
                .build();
    }

    private RoomBo getRoom(String senderId, String recipientId) {
        Optional<RoomBo> roomBo = roomRepository.findBySenderIdAndRecipientId(senderId, recipientId);
        if (roomBo.isPresent())
            return roomBo.get();
        else
            return initiateRoom(senderId, recipientId);
    }

    private RoomBo initiateRoom(String senderId, String recipientId) {
        String chatId = UUID.randomUUID().toString();
        roomRepository.save(RoomBo.builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build());
        roomRepository.save(RoomBo.builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build());
        return RoomBo.builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();
    }
}
