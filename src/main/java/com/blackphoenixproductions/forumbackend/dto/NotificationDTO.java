package com.blackphoenixproductions.forumbackend.dto;

import com.blackphoenixproductions.forumbackend.entity.Topic;
import com.blackphoenixproductions.forumbackend.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO implements Comparable<NotificationDTO>{

    private Long id;
    @JsonIgnoreProperties({"topics", "posts"})
    private User fromUser;
    @JsonIgnoreProperties({"topics", "posts"})
    private User toUser;
    @JsonIgnoreProperties({"user", "posts"})
    private Topic topic;
    private String message;
    private LocalDateTime createDate;
    private String url;

    @Override
    public int compareTo(NotificationDTO o) {
        return o.createDate.compareTo(this.createDate);
    }
}
