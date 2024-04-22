package com.test.map;

import com.test.bo.MessageBo;
import com.test.entity.MessageEntity;

public interface MessageMap {

    MessageBo toBo(MessageEntity entity);

    MessageEntity toEntity(MessageBo bo);
}
