package com.test.service;

import com.test.request.PushNotificationRequest;

public interface NotificationService {

    void notifyReceiver(PushNotificationRequest request);
}
