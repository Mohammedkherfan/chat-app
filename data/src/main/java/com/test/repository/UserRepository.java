package com.test.repository;

import com.test.bo.UserBo;
import com.test.enums.ChatStatus;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    UserBo save(UserBo userBo);

    Boolean existsByUsername(String username);

    Optional<UserBo> findByUsername(String username);

    List<UserBo> findAllByChatStatus(ChatStatus chatStatus);
}
