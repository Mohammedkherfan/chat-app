package com.test.repository;

import com.test.bo.MessageBo;

import java.util.List;

public interface MessageRepository {

    MessageBo save(MessageBo messageBo);

    List<MessageBo> findAllByChatId(String chatId);
}
