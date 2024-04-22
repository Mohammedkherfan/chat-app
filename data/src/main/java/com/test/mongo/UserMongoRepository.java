package com.test.mongo;

import com.test.entity.UserEntity;
import com.test.enums.ChatStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMongoRepository extends MongoRepository<UserEntity, Long> {

    Boolean existsByUsername(String username);

    UserEntity findByUsername(String username);

    List<UserEntity> findAllByChatStatus(ChatStatus chatStatus);
}
