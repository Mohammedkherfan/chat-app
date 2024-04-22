package com.test.repository.impl;

import com.test.map.MessageMap;
import com.test.mongo.MessageMongoRepository;
import com.test.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageRepositoryImpl implements MessageRepository {

    @Autowired
    private MessageMongoRepository repository;
    @Autowired
    private MessageMap map;
}
