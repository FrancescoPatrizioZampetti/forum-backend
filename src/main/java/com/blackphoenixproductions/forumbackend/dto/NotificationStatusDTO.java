package com.blackphoenixproductions.forumbackend.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@Data
@RedisHash("UserNotificationStatusDTO")
public class NotificationStatusDTO {

    @Id
    private String username;
    private Boolean newNotification;
}
