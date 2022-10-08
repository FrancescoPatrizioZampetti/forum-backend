package com.blackphoenixproductions.forumbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("UserNotificationStatusDTO")
public class NotificationStatusDTO {

    @Id
    private String username;
    private Boolean allReaded;
}
