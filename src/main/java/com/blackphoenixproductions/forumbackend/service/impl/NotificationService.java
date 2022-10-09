package com.blackphoenixproductions.forumbackend.service.impl;

import com.blackphoenixproductions.forumbackend.dto.NotificationDTO;
import com.blackphoenixproductions.forumbackend.dto.NotificationStatusDTO;
import com.blackphoenixproductions.forumbackend.entity.Post;
import com.blackphoenixproductions.forumbackend.entity.User;
import com.blackphoenixproductions.forumbackend.enums.Pagination;
import com.blackphoenixproductions.forumbackend.redis.RedisMessagePublisher;
import com.blackphoenixproductions.forumbackend.repository.NotificationRepository;
import com.blackphoenixproductions.forumbackend.repository.NotificationStatusRepository;
import com.blackphoenixproductions.forumbackend.service.INotificationService;
import com.blackphoenixproductions.forumbackend.service.IPostService;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class NotificationService implements INotificationService {
    private final int MAX_NOTIFICATION_LENGTH = 20;
    private final IPostService postService;
    private final NotificationRepository notificationRepository;
    private final NotificationStatusRepository notificationStatusRepository;
    private final RedisMessagePublisher redisMessagePublisher;


    @Autowired
    public NotificationService(IPostService postService, NotificationRepository notificationRepository, NotificationStatusRepository notificationStatusRepository, RedisMessagePublisher redisMessagePublisher) {
        this.postService = postService;
        this.notificationRepository = notificationRepository;
        this.notificationStatusRepository = notificationStatusRepository;
        this.redisMessagePublisher = redisMessagePublisher;
    }

    @Transactional
    @Override
    public void notifyTopicAuthor(Post post) {
        if (userIsNotTopicAuthor(post)){
            String topicAuthorUsername = post.getTopic().getUser().getUsername();
            NotificationDTO notification = createUserNotification(post);
            // todo valutare durata notifiche ed ordinamento
            notificationRepository.save(notification);
            notificationStatusRepository.save(new NotificationStatusDTO(topicAuthorUsername, true));
            redisMessagePublisher.publish(topicAuthorUsername);
        }
    }

    private boolean userIsNotTopicAuthor(Post post) {
        return !post.getUser().equals(post.getTopic().getUser());
    }

    private NotificationDTO createUserNotification(Post post) {
        NotificationDTO notification = getNotificationDTO(post);
        int lastTopicPageNumber = postService.getPagedPosts(post.getTopic().getId(), PageRequest.of(0, Math.toIntExact(Pagination.POST_PAGINATION.getValue()), Sort.by("createDate").ascending())).getTotalPages()-1;
        notification.setUrl("/viewtopic?id=" + post.getTopic().getId() + "&page=" + lastTopicPageNumber);
        return notification;
    }

    private NotificationDTO getNotificationDTO(Post post) {
        NotificationDTO notification = new NotificationDTO();
        notification.setFromUser(post.getUser().getUsername());
        notification.setToUser(post.getTopic().getUser().getUsername());
        notification.setTopicTitle(post.getTopic().getTitle());
        notification.setCreateDate(LocalDateTime.now());
        notification.setMessage(setNotificationMessage(post.getMessage()));
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
    public List<NotificationDTO> getUserNotification(User user) {
        List<NotificationDTO> userNotifications = notificationRepository.findByFromUser(user.getUsername());
        return userNotifications;
    }

    @Override
    public Boolean getUserNotificationStatus(User user) {
        Optional<NotificationStatusDTO> notificationsStatus = null;
        notificationsStatus = notificationStatusRepository.findById(user.getUsername());
        if(notificationsStatus.isPresent()){
            return notificationsStatus.get().getNewNotification();
        }
        return false;
    }

    @Override
    public void setNotificationStatus(String username, boolean showNotificationNotice) {
        notificationStatusRepository.save(new NotificationStatusDTO(username, showNotificationNotice));
    }

}
