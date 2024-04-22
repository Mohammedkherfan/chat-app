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

import java.time.LocalDateTime;
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
        RoomBo roomBo = getRoom(request.getSenderId(), request.getReceiverId());
        MessageBo messageBo = messageRepository.save(MessageBo.builder()
                .messageExternalId(UUID.randomUUID().toString())
                .chatId(roomBo.getChatId())
                .senderId(request.getSenderId())
                .receiverId(request.getReceiverId())
                .body(request.getBody())
                .dateTime(LocalDateTime.now())
                .build());
        return PushMessageResponse.builder()
                .messageExternalId(messageBo.getMessageExternalId())
                .chatId(messageBo.getChatId())
                .senderId(messageBo.getSenderId())
                .receiverId(messageBo.getReceiverId())
                .body(messageBo.getBody())
                .dateTime(messageBo.getDateTime())
                .build();
    }

    @Override
    public ListMessagesResponse list(String senderId, String receiverId) {
        Optional<RoomBo> roomBo = roomRepository.findBySenderIdAndReceiverId(senderId, receiverId);
        if (roomBo.isEmpty())
            throw new ChatAppException("Invalid room");
        List<MessageDto> messages = messageRepository.findAllByChatId(roomBo.get().getChatId()).stream().map(bo -> MessageDto.builder()
                .messageExternalId(bo.getMessageExternalId())
                .chatId(bo.getChatId())
                .senderId(bo.getSenderId())
                .receiverId(bo.getReceiverId())
                .body(bo.getBody())
                .dateTime(bo.getDateTime())
                .build())
                .toList();
        return ListMessagesResponse.builder()
                .messages(messages)
                .build();
    }

    private RoomBo getRoom(String senderId, String receiverId) {
        Optional<RoomBo> roomBo = roomRepository.findBySenderIdAndReceiverId(senderId, receiverId);
        if (roomBo.isPresent())
            return roomBo.get();
        else
            return initiateRoom(senderId, receiverId);
    }

    private RoomBo initiateRoom(String senderId, String receiverId) {
        String roomExternalId = UUID.randomUUID().toString();
        String chatId = UUID.randomUUID().toString();
        roomRepository.save(RoomBo.builder()
                .roomExternalId(roomExternalId)
                .chatId(chatId)
                .senderId(senderId)
                .receiverId(receiverId)
                .build());
        roomRepository.save(RoomBo.builder()
                .roomExternalId(roomExternalId)
                .chatId(chatId)
                .senderId(receiverId)
                .receiverId(senderId)
                .build());
        return RoomBo.builder()
                .roomExternalId(roomExternalId)
                .chatId(chatId)
                .senderId(senderId)
                .receiverId(receiverId)
                .build();
    }
}
