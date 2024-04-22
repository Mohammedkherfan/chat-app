package com.test.repository.impl;

import com.test.bo.RoomBo;
import com.test.entity.RoomEntity;
import com.test.map.RoomMap;
import com.test.mongo.RoomMongoRepository;
import com.test.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class RoomRepositoryImpl implements RoomRepository {

    @Autowired
    private RoomMongoRepository repository;
    @Autowired
    private RoomMap map;

    @Override
    public Optional<RoomBo> findBySenderIdAndReceiverId(String senderId, String receiverId) {
        RoomEntity entity = repository.findBySenderIdAndReceiverId(senderId, receiverId);
        if (Objects.isNull(entity))
            return Optional.empty();
        else
            return Optional.of(map.toBo(entity));
    }

    @Override
    public RoomBo save(RoomBo roomBo) {
        return map.toBo(repository.save(map.toEntity(roomBo)));
    }
}
