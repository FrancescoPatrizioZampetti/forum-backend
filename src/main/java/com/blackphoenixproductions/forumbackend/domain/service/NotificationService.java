package com.blackphoenixproductions.forumbackend.domain.service;

import com.blackphoenixproductions.forumbackend.domain.ports.INotificationService;
import com.blackphoenixproductions.forumbackend.domain.ports.IPostService;
import com.blackphoenixproductions.forumbackend.domain.ports.MessagePublisher;
import com.blackphoenixproductions.forumbackend.domain.model.Notification;
import com.blackphoenixproductions.forumbackend.domain.model.NotificationStatus;
import com.blackphoenixproductions.forumbackend.domain.model.Post;
import com.blackphoenixproductions.forumbackend.domain.model.User;
import com.blackphoenixproductions.forumbackend.domain.enums.Pagination;
import com.blackphoenixproductions.forumbackend.domain.ports.repository.NotificationRepository;
import com.blackphoenixproductions.forumbackend.domain.ports.repository.NotificationStatusRepository;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService implements INotificationService {
    private final int MAX_NOTIFICATION_LENGTH = 20;
    private final IPostService postService;
    private final NotificationRepository notificationRepository;
    private final NotificationStatusRepository notificationStatusRepository;
    private final MessagePublisher messagePublisher;


    @Autowired
    public NotificationService(IPostService postService, NotificationRepository notificationRepository, NotificationStatusRepository notificationStatusRepository, MessagePublisher messagePublisher) {
        this.postService = postService;
        this.notificationRepository = notificationRepository;
        this.notificationStatusRepository = notificationStatusRepository;
        this.messagePublisher = messagePublisher;
    }

    @Transactional
    @Override
    public void notifyTopicAuthor(Post post) {
        if (userIsNotTopicAuthor(post)){
            String topicAuthorUsername = post.getTopic().getUser().getUsername();
            Notification notification = createUserNotification(post);
            notificationRepository.save(notification);
            notificationStatusRepository.save(new NotificationStatus(topicAuthorUsername, true));
            messagePublisher.publish(topicAuthorUsername);
        }
    }

    private boolean userIsNotTopicAuthor(Post post) {
        return !post.getUser().equals(post.getTopic().getUser());
    }

    private Notification createUserNotification(Post post) {
        Notification notification = createNotification(post);
        int lastTopicPageNumber = postService.getPagedPosts(post.getTopic().getId(), PageRequest.of(0, Math.toIntExact(Pagination.POST_PAGINATION.getValue()), Sort.by("createDate").ascending())).getTotalPages()-1;
        notification.setUrl("/viewtopic?id=" + post.getTopic().getId() + "&page=" + lastTopicPageNumber);
        return notification;
    }

    private Notification createNotification(Post post) {
        Notification notification = new Notification();
        notification.setFromUser(post.getUser().getUsername());
        notification.setToUser(post.getTopic().getUser().getUsername());
        notification.setTopicTitle(post.getTopic().getTitle());
        notification.setCreateDate(LocalDateTime.now());
        notification.setMessage(setNotificationMessage(post.getMessage()));
        notification.setFromUserRole(post.getUser().getRole());
        return notification;
    }

    private String setNotificationMessage(String message) {
        message = Jsoup.clean(message, Safelist.none());
        int messageSize = message.length();
        if (messageSize > MAX_NOTIFICATION_LENGTH) {
            message = message.substring(0, MAX_NOTIFICATION_LENGTH);
            message += " [...]";
        }
        return message;
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
