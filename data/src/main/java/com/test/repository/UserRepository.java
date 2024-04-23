package com.test.repository;

import com.test.bo.UserBo;
import com.test.enums.Status;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    UserBo save(UserBo userBo);

    Boolean existsByNickName(String nickName);

    Optional<UserBo> findByNickName(String nickName);

    List<UserBo> findAllByStatus(Status status);
}
