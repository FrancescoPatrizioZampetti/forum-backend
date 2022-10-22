package com.blackphoenixproductions.forumbackend.domain.ports;


import com.blackphoenixproductions.forumbackend.domain.entity.Notification;
import com.blackphoenixproductions.forumbackend.domain.entity.Post;
import com.blackphoenixproductions.forumbackend.domain.entity.User;

import java.util.List;

public interface INotificationService {

    void notifyTopicAuthor(Post post);

    List<Notification> getUserNotification(User user);

    Boolean getUserNotificationStatus(User user);

    void setNotificationStatus(String username, boolean showNotificationNotice);

}
