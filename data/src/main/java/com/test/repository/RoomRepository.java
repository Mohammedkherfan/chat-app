package com.test.repository;

import com.test.bo.RoomBo;

import java.util.Optional;

public interface RoomRepository {

    Optional<RoomBo> findBySenderIdAndRecipientId(String senderId, String recipientId);

    RoomBo save(RoomBo roomBo);
}
