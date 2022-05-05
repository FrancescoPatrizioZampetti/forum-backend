package com.blackphoenixproductions.forumbackend.service;


import com.blackphoenixproductions.forumbackend.dto.NotificationDTO;
import com.blackphoenixproductions.forumbackend.entity.Post;
import com.blackphoenixproductions.forumbackend.entity.User;

import java.util.List;

public interface INotificationService {

    void notifyTopicAuthor(Post post);

    List<NotificationDTO> getUserNotification(User user);

    Boolean getUserNotificationStatus(User user);

    void setReadedNotificationStatus(User user);

    void removeOldestNotification(List<NotificationDTO> userNotificationList);

}
