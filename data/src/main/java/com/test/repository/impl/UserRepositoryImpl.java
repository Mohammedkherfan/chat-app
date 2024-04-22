package com.test.repository.impl;

import com.test.bo.UserBo;
import com.test.entity.UserEntity;
import com.test.enums.ChatStatus;
import com.test.map.UserMap;
import com.test.mongo.UserMongoRepository;
import com.test.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private UserMongoRepository repository;
    @Autowired
    private UserMap map;

    @Override
    public UserBo save(UserBo userBo) {
        return map.toBo(repository.save(map.toEntity(userBo)));
    }

    @Override
    public Boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public Optional<UserBo> findByUsername(String username) {
        UserEntity entity = repository.findByUsername(username);
        if (Objects.isNull(entity))
            return Optional.empty();
        else
            return Optional.of(map.toBo(entity));
    }

    @Override
    public List<UserBo> findAllByChatStatus(ChatStatus chatStatus) {
        return repository.findAllByChatStatus(chatStatus);
    }
}
