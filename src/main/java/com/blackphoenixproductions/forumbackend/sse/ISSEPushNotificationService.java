package com.blackphoenixproductions.forumbackend.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface ISSEPushNotificationService {

    void sendHeartBeatEvent();
    void addEmitter(String username, final SseEmitter emitter);
    void removeEmitter(final SseEmitter emitter);
    void initEvent (String username);
    void heartBeatEvent(String username);
    void sendNotification(String username);
}
