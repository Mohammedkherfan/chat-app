package com.test.map.impl;

import com.test.bo.RoomBo;
import com.test.entity.RoomEntity;
import com.test.map.RoomMap;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class RoomMapImpl implements RoomMap {

    @Override
    public RoomBo toBo(RoomEntity entity) {
        RoomBo bo = new RoomBo();
        BeanUtils.copyProperties(entity, bo);
        return bo;
    }

    @Override
    public RoomEntity toEntity(RoomBo bo) {
        RoomEntity entity = new RoomEntity();
        BeanUtils.copyProperties(bo, entity);
        return entity;
    }
}
