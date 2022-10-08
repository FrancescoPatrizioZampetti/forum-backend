package com.blackphoenixproductions.forumbackend.redis;

import com.blackphoenixproductions.forumbackend.sse.ISSEPushNotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RedisMessageSubscriber implements MessageListener {

    private ISSEPushNotificationService ssePushNotificationService;
    public static List<String> messageList = new ArrayList<String>();

    public RedisMessageSubscriber(ISSEPushNotificationService ssePushNotificationService) {
        this.ssePushNotificationService = ssePushNotificationService;
    }

    public void onMessage(final Message message, final byte[] pattern) {
        messageList.add(message.toString());
        System.out.println("Message received: " + new String(message.getBody()));
        ssePushNotificationService.sendNotification(new String(message.getBody()));
    }

}
