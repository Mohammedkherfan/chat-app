package com.test.map.impl;

import com.test.bo.MessageBo;
import com.test.entity.MessageEntity;
import com.test.map.MessageMap;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class MessageMapImpl implements MessageMap {

    @Override
    public MessageBo toBo(MessageEntity entity) {
        MessageBo bo = new MessageBo();
        BeanUtils.copyProperties(entity, bo);
        return bo;
    }

    @Override
    public MessageEntity toEntity(MessageBo bo) {
        MessageEntity entity = new MessageEntity();
        BeanUtils.copyProperties(bo, entity);
        return entity;
    }
}
