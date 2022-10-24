package com.blackphoenixproductions.forumbackend.domain.service;

import com.blackphoenixproductions.forumbackend.domain.factory.NotificationSimpleFactory;
import com.blackphoenixproductions.forumbackend.domain.INotificationService;
import com.blackphoenixproductions.forumbackend.domain.MessagePublisher;
import com.blackphoenixproductions.forumbackend.domain.entity.Notification;
import com.blackphoenixproductions.forumbackend.domain.entity.NotificationStatus;
import com.blackphoenixproductions.forumbackend.domain.entity.Post;
import com.blackphoenixproductions.forumbackend.domain.entity.User;
import com.blackphoenixproductions.forumbackend.domain.repository.NotificationRepository;
import com.blackphoenixproductions.forumbackend.domain.repository.NotificationStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService implements INotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationStatusRepository notificationStatusRepository;
    private final MessagePublisher messagePublisher;
    private final NotificationSimpleFactory notificationSimpleFactory;


    @Autowired
    public NotificationService(NotificationRepository notificationRepository, NotificationStatusRepository notificationStatusRepository,
                               MessagePublisher messagePublisher, NotificationSimpleFactory notificationSimpleFactory) {
        this.notificationRepository = notificationRepository;
        this.notificationStatusRepository = notificationStatusRepository;
        this.messagePublisher = messagePublisher;
        this.notificationSimpleFactory = notificationSimpleFactory;
    }

    @Transactional
    @Override
    public void notifyTopicAuthor(Post post) {
        if (userIsNotTopicAuthor(post)){
            String topicAuthorUsername = post.getTopic().getUser().getUsername();
            Notification notification = notificationSimpleFactory.createUserNotification(post);
            notificationRepository.save(notification);
            notificationStatusRepository.save(new NotificationStatus(topicAuthorUsername, true));
            messagePublisher.publish(topicAuthorUsername);
        }
    }

    private boolean userIsNotTopicAuthor(Post post) {
        return !post.getUser().equals(post.getTopic().getUser());
    }

    @Override
    public List<Notification> getUserNotification(User user) {
        List<Notification> userNotifications = notificationRepository.findAllByToUserOrderByCreateDateDesc(user.getUsername());
        return userNotifications;
    }

    @Override
    public Boolean getUserNotificationStatus(User user) {
        Boolean status = null;
        Optional<NotificationStatus> notificationsStatus = notificationStatusRepository.findById(user.getUsername());
        if(notificationsStatus.isPresent()){
            status = notificationsStatus.get().getNewNotification();
        }
        return status;
    }

    @Override
    public void setNotificationStatus(String username, boolean showNotificationNotice) {
        notificationStatusRepository.save(new NotificationStatus(username, showNotificationNotice));
    }

}
