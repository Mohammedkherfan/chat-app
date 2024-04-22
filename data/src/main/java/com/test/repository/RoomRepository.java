package com.test.repository;

import com.test.bo.RoomBo;

import java.util.Optional;

public interface RoomRepository {

    Optional<RoomBo> findBySenderIdAndReceiverId(String senderId, String receiverId);

    RoomBo save(RoomBo roomBo);
}
