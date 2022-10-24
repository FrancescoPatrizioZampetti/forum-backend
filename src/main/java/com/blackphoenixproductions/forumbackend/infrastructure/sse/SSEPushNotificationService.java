package com.blackphoenixproductions.forumbackend.infrastructure.sse;

import com.blackphoenixproductions.forumbackend.domain.ISSEPushNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SSEPushNotificationService implements ISSEPushNotificationService {

    private final DateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(SSEPushNotificationService.class);


    public void sendHeartBeatEvent(){
        emitters.keySet().stream().forEach(username -> heartBeatEvent(username));
    }

    public void addEmitter(String username, final SseEmitter emitter) {
        emitters.put(username, emitter);
    }

    public void removeEmitter(final SseEmitter emitter) {
        emitters.remove(emitter);
    }

    public void initEvent (String username) {
        try {
            SseEmitter userEmitter = emitters.get(username);
            userEmitter.send(SseEmitter.event()
                    .data(DATE_FORMATTER.format(new Date()) + " : " + UUID.randomUUID().toString() + " -> " + username));
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    public void heartBeatEvent(String username) {
        try {
            SseEmitter userEmitter = emitters.get(username);
            if(userEmitter != null) {
                userEmitter.send(SseEmitter.event()
                        .data(""));
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    public void sendNotification(String username) {
        try {
            SseEmitter userEmitter = emitters.get(username);
            if(userEmitter != null) {
                logger.info("Invio notifica ad User : {}", username);
                userEmitter.send(SseEmitter.event()
                        .data("new notification"));
            } else {
                logger.warn("Utente non notificabile");
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

}