package com.test.map;

import com.test.bo.UserBo;
import com.test.entity.UserEntity;

public interface UserMap {

    UserBo toBo(UserEntity entity);

    UserEntity toEntity(UserBo bo);
}
