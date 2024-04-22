package com.test.map;

import com.test.bo.RoomBo;
import com.test.entity.RoomEntity;

public interface RoomMap {

    RoomBo toBo(RoomEntity entity);

    RoomEntity toEntity(RoomBo bo);
}
