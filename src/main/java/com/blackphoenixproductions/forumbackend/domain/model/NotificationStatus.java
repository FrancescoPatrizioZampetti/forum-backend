package com.blackphoenixproductions.forumbackend.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@Data
@RedisHash("NotificationStatus")
public class NotificationStatus {

    @Id
    private String username;
    private Boolean newNotification;
}
