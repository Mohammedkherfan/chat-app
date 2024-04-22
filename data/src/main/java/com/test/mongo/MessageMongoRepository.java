package com.test.mongo;

import com.test.bo.MessageBo;
import com.test.entity.MessageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageMongoRepository extends MongoRepository<MessageEntity, Long> {

    List<MessageEntity> findAllByChatId(String chatId);
}
