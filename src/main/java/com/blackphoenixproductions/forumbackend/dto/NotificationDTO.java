package com.blackphoenixproductions.forumbackend.dto;

import com.blackphoenixproductions.forumbackend.entity.Topic;
import com.blackphoenixproductions.forumbackend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO implements Comparable<NotificationDTO>{

    private Long id;
    private User fromUser;
    private User toUser;
    private Topic topic;
    private String message;
    private Date createDate;
    private String timeDifferenceFromNow;
    private String url;

    @Override
    public int compareTo(NotificationDTO o) {
        return o.createDate.compareTo(this.createDate);
    }
}
