package com.blackphoenixproductions.forumbackend.dto;

import dto.SimpleTopicDTO;
import dto.SimpleUserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO implements Comparable<NotificationDTO>{

    private Long id;
    private dto.SimpleUserDTO fromUser;
    private SimpleUserDTO toUser;
    private SimpleTopicDTO topic;
    private String message;
    private Date createDate;
    private String timeDifferenceFromNow;
    private String url;

    @Override
    public int compareTo(NotificationDTO o) {
        return o.createDate.compareTo(this.createDate);
    }
}
