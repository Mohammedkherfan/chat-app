package com.test.repository.impl;

import com.test.bo.UserBo;
import com.test.entity.UserEntity;
import com.test.enums.Status;
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
    public Boolean existsByNickName(String nickName) {
        return repository.existsByNickName(nickName);
    }

    @Override
    public Optional<UserBo> findByNickName(String nickName) {
        UserEntity entity = repository.findByNickName(nickName);
        if (Objects.isNull(entity))
            return Optional.empty();
        else
            return Optional.of(map.toBo(entity));
    }

    public List<UserBo> findAllByStatus(Status status) {
        return repository.findAllByStatus(status).stream().map(entity -> map.toBo(entity)).toList();
    }
}
