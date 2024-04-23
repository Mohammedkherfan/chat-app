package com.test.mongo;

import com.test.entity.UserEntity;
import com.test.enums.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMongoRepository extends MongoRepository<UserEntity, Long> {

    Boolean existsByNickName(String username);

    UserEntity findByNickName(String username);

    List<UserEntity> findAllByStatus(Status status);
}
