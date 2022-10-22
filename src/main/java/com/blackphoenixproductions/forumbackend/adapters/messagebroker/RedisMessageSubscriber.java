package com.blackphoenixproductions.forumbackend.adapters.messagebroker;

import com.blackphoenixproductions.forumbackend.domain.ports.ISSEPushNotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;


public class RedisMessageSubscriber implements MessageListener {

    private ISSEPushNotificationService ssePushNotificationService;

    public RedisMessageSubscriber(ISSEPushNotificationService ssePushNotificationService) {
        this.ssePushNotificationService = ssePushNotificationService;
    }

    public void onMessage(final Message message, final byte[] pattern) {
        ssePushNotificationService.sendNotification(new String(message.getBody()));
    }

}
