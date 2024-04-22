package com.test.service.impl;

import com.test.bo.RoomBo;
import com.test.exception.ChatAppException;
import com.test.repository.RoomRepository;
import com.test.response.GetRoomResponse;
import com.test.service.RoomService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public GetRoomResponse initiate(String senderId, String receiverId, Boolean createNewRoomIfNotExist) {
        Optional<RoomBo> roomBo = roomRepository.findBySenderIdAndReceiverId(senderId, receiverId);
        if (roomBo.isPresent())
            return GetRoomResponse.builder()
                    .roomExternalId(roomBo.get().getRoomExternalId())
                    .chatId(roomBo.get().getChatId())
                    .senderId(roomBo.get().getSenderId())
                    .receiverId(roomBo.get().getReceiverId())
                    .build();

        if (createNewRoomIfNotExist) {
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
            return GetRoomResponse.builder()
                    .roomExternalId(roomExternalId)
                    .chatId(chatId)
                    .senderId(senderId)
                    .receiverId(receiverId)
                    .build();
        }
        throw new ChatAppException("Can not find or initiate chat room");
    }
}
