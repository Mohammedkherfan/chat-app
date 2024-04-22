package com.test.map.impl;

import com.test.bo.UserBo;
import com.test.entity.UserEntity;
import com.test.map.UserMap;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserMapImpl implements UserMap {

    @Override
    public UserBo toBo(UserEntity entity) {
        UserBo bo = new UserBo();
        BeanUtils.copyProperties(entity, bo);
        return bo;
    }

    @Override
    public UserEntity toEntity(UserBo bo) {
        UserEntity entity = new UserEntity();
        BeanUtils.copyProperties(bo, entity);
        return entity;
    }
}
