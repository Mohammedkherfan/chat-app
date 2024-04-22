package com.test.service;

import com.test.request.PushMessageRequest;
import com.test.response.ListMessagesResponse;
import com.test.response.PushMessageResponse;

public interface MessageService {

    PushMessageResponse push(PushMessageRequest request);

    ListMessagesResponse list(String senderId, String receiverId);
}
