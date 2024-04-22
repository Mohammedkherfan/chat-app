package com.test.service;

import com.test.response.GetRoomResponse;

public interface RoomService {

    GetRoomResponse initiate(String senderId, String receiverId, Boolean createNewRoomIfNotExist);

}
