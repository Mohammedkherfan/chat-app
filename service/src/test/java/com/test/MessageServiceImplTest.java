package com.test;

import com.test.bo.MessageBo;
import com.test.bo.RoomBo;
import com.test.exception.ChatAppException;
import com.test.repository.MessageRepository;
import com.test.repository.RoomRepository;
import com.test.request.PushMessageRequest;
import com.test.response.ListMessagesResponse;
import com.test.response.PushMessageResponse;
import com.test.service.impl.MessageServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class MessageServiceImplTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private MessageServiceImpl messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void push_ValidRequest_ReturnsPushMessageResponse() {
        // Arrange
        PushMessageRequest request = new PushMessageRequest("id","chatId","senderId", "recipientId", "content", new Date());
        RoomBo roomBo = new RoomBo("id","chatId", "senderId", "recipientId");
        MessageBo savedMessage = new MessageBo("messageId", "chatId", "senderId", "recipientId", "content", new Date());
        when(roomRepository.findBySenderIdAndRecipientId("senderId", "recipientId")).thenReturn(Optional.of(roomBo));
        when(messageRepository.save(any())).thenReturn(savedMessage);

        // Act
        PushMessageResponse response = messageService.push(request);

        // Assert
        assertNotNull(response);
        assertEquals(savedMessage.getId(), response.getId());
        assertEquals(savedMessage.getChatId(), response.getChatId());
        assertEquals(savedMessage.getSenderId(), response.getSenderId());
        assertEquals(savedMessage.getRecipientId(), response.getRecipientId());
        assertEquals(savedMessage.getContent(), response.getContent());
        assertEquals(savedMessage.getTimestamp(), response.getTimestamp());
        verify(roomRepository, times(1)).findBySenderIdAndRecipientId("senderId", "recipientId");
        verify(messageRepository, times(1)).save(any());
    }

    @Test
    void testListMessagesInvalidRoom() {
        String senderId = "senderId";
        String recipientId = "recipientId";

        // Mock repository to return empty optional for room
        when(roomRepository.findBySenderIdAndRecipientId(senderId, recipientId)).thenReturn(Optional.empty());

        // Verify that ChatAppException is thrown for invalid room
        Assert.assertThrows(ChatAppException.class, () -> messageService.list(senderId, recipientId));
    }

    @Test
    void testListMessagesValidRoom() {
        String senderId = "senderId";
        String recipientId = "recipientId";
        String chatId = "chatId";

        // Mock RoomBo
        RoomBo roomBo = new RoomBo();
        roomBo.setChatId(chatId);

        // Mock repository to return RoomBo
        when(roomRepository.findBySenderIdAndRecipientId(senderId, recipientId)).thenReturn(Optional.of(roomBo));

        // Mock MessageBo list
        List<MessageBo> messageBos = new ArrayList<>();
        messageBos.add(new MessageBo(/* populate message fields */)); // Add some message objects

        // Mock repository to return messageBos
        when(messageRepository.findAllByChatId(chatId)).thenReturn(messageBos);

        // Call list method
        ListMessagesResponse response = messageService.list(senderId, recipientId);

        // Verify that ListMessagesResponse is returned with correct messages
        assertNotNull(response);
        assertNotNull(response.getMessages());
        assertEquals(messageBos.size(), response.getMessages().size()); // Check if all messages are returned
        // Add more assertions to check if MessageDto objects are correctly mapped from MessageBo objects
    }
}

