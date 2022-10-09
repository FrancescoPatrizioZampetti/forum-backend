package com.blackphoenixproductions.forumbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("NotificationDTO")
public class NotificationDTO {

    @Id
    private String id;
    @Indexed
    private String fromUser;
    private String fromUserRole;
    private String toUser;
    private String topicTitle;
    private String message;
    private String url;
    private LocalDateTime createDate;

}
