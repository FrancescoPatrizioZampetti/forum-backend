package com.blackphoenixproductions.forumbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("NotificationDTO")
public class NotificationDTO implements Comparable<NotificationDTO>{

    @Id
    private String id;
    private String fromUser;
    private String fromUserRole;
    private String toUser;
    private String topicTitle;
    private String message;
    private String url;
    private LocalDateTime createDate;

    @Override
    public int compareTo(NotificationDTO o) {
        return o.createDate.compareTo(this.createDate);
    }
}
