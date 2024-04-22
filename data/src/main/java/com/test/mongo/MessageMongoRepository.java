package com.test.mongo;

import com.test.entity.MessageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageMongoRepository extends MongoRepository<MessageEntity, Long> {

}
