package com.test.mongo;

import com.test.entity.RoomEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomMongoRepository extends MongoRepository<RoomEntity, Long> {

    RoomEntity findBySenderIdAndReceiverId(String senderId, String receiverId);
}
