package com.blackphoenixproductions.forumbackend.domain.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.GeneratedValue;
import java.time.LocalDateTime;

@Data
@RedisHash("Notification")
public class Notification {

    @Id
    @GeneratedValue
    private String id;
    @Indexed
    private String fromUser;
    private String fromUserRole;
    @Indexed
    private String toUser;
    private String topicTitle;
    private String message;
    private String url;
    private LocalDateTime createDate;

}
