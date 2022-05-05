package com.blackphoenixproductions.forumbackend.service;

import dto.NotificationDTO;
import dto.PostDTO;
import dto.SimpleUserDTO;

import java.util.List;

public interface INotificationService {

    void notifyTopicAuthor(PostDTO postDTO);

    List<NotificationDTO> getUserNotification(SimpleUserDTO simpleUserDTO);

    Boolean getUserNotificationStatus(SimpleUserDTO simpleUserDTO);

    void setReadedNotificationStatus(SimpleUserDTO simpleUserDTO);

    void removeOldestNotification(List<NotificationDTO> userNotificationList);

}
