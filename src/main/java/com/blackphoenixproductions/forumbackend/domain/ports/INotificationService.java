package com.blackphoenixproductions.forumbackend.domain.ports;


import com.blackphoenixproductions.forumbackend.domain.model.Notification;
import com.blackphoenixproductions.forumbackend.domain.model.Post;
import com.blackphoenixproductions.forumbackend.domain.model.User;

import java.util.List;

public interface INotificationService {

    void notifyTopicAuthor(Post post);

    List<Notification> getUserNotification(User user);

    Boolean getUserNotificationStatus(User user);

    void setNotificationStatus(String username, boolean showNotificationNotice);

}
