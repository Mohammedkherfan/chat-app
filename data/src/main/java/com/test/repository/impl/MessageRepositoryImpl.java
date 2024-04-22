package com.test.repository.impl;

import com.test.bo.MessageBo;
import com.test.map.MessageMap;
import com.test.mongo.MessageMongoRepository;
import com.test.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageRepositoryImpl implements MessageRepository {

    @Autowired
    private MessageMongoRepository repository;
    @Autowired
    private MessageMap map;

    @Override
    public MessageBo save(MessageBo messageBo) {
        return map.toBo(repository.save(map.toEntity(messageBo)));
    }

    @Override
    public List<MessageBo> findAllByChatId(String chatId) {
        return repository.findAllByChatId(chatId).stream().map(entity -> map.toBo(entity)).toList();
    }
}
