package com.blackphoenixproductions.forumbackend.service.impl;

import com.blackphoenixproductions.forumbackend.dto.NotificationDTO;
import com.blackphoenixproductions.forumbackend.entity.Post;
import com.blackphoenixproductions.forumbackend.entity.User;
import com.blackphoenixproductions.forumbackend.enums.Pagination;
import com.blackphoenixproductions.forumbackend.service.INotificationService;
import com.blackphoenixproductions.forumbackend.service.IPostService;
import com.blackphoenixproductions.forumbackend.sse.SsePushNotificationService;
import com.blackphoenixproductions.forumbackend.utility.DateUtility;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class NotificationService implements INotificationService {

    private static final Map<String, List<NotificationDTO>> notificationStore = new ConcurrentHashMap<>();
    private static final Map<String, Boolean> usersNotificationStatus = new ConcurrentHashMap<>();
    private static final AtomicLong idCounter = new AtomicLong();
    private final SsePushNotificationService ssePushNotificationService;
    private final int MAX_UNREADED_NOTIFICATIONS = 10;
    private final int MAX_NOTIFICATION_LENGTH = 20;
    private final IPostService postService;


    @Autowired
    public NotificationService(SsePushNotificationService ssePushNotificationService, IPostService postService) {
        this.ssePushNotificationService = ssePushNotificationService;
        this.postService = postService;
    }

    @Transactional
    @Override
    public void notifyTopicAuthor(Post post) {
        if (userIsNotTopicAuthor(post)){
            String topicAuthorUsername = post.getTopic().getUser().getUsername();
            NotificationDTO notification = createUserNotification(post);
            List<NotificationDTO> userNotificationList = addToUserNotifications(topicAuthorUsername, notification);
            removeOldestNotification(userNotificationList);
            updateStatusAndstoreUserNotification(topicAuthorUsername, userNotificationList);
            // invio sse event per notifica push
            ssePushNotificationService.sendNotificationToTopicAuthor(topicAuthorUsername);
        }
    }

    private boolean userIsNotTopicAuthor(Post post) {
        return !post.getUser().equals(post.getTopic().getUser());
    }

    private NotificationDTO createUserNotification(Post post) {
        NotificationDTO notification = new NotificationDTO();
        notification.setId(idCounter.getAndIncrement());
        notification.setFromUser(post.getUser());
        notification.setToUser(post.getTopic().getUser());
        notification.setTopic(post.getTopic());
        notification.setCreateDate(new Date());
        notification.setTimeDifferenceFromNow(DateUtility.setTimeDifferenceFromNow(notification.getCreateDate()));
        notification.setMessage(setNotificationMessage(post.getMessage()));
        int lastTopicPageNumber = postService.getPagedPosts(post.getTopic().getId(), PageRequest.of(0, Math.toIntExact(Pagination.POST_PAGINATION.getValue()), Sort.by("createDate").ascending())).getTotalPages()-1;
        notification.setUrl("/viewtopic?id=" + post.getTopic().getId() + "&page=" + lastTopicPageNumber);
        return notification;
    }

    private void updateStatusAndstoreUserNotification(String username, List<NotificationDTO> userNotificationList) {
        notificationStore.put(username, userNotificationList);
        usersNotificationStatus.put(username, true);
    }

    private List<NotificationDTO> addToUserNotifications(String username, NotificationDTO notification) {
        List<NotificationDTO> userNotificationList = notificationStore.get(username);
        if(userNotificationList == null){
            userNotificationList = new ArrayList<>();
        }
        userNotificationList.add(notification);
        // ordino per data crescente
        Collections.sort(userNotificationList);
        return userNotificationList;
    }

    private String setNotificationMessage(String message) {
        message = Jsoup.clean(message, Whitelist.none());
        int messageSize = message.length();
        if (messageSize > MAX_NOTIFICATION_LENGTH) {
            message = message.substring(0, MAX_NOTIFICATION_LENGTH);
            message += " [...]";
        }
        return message;
    }

    @Override
    public List<NotificationDTO> getUserNotification(User user) {
        List<NotificationDTO> userNotifications = notificationStore.get(user.getUsername());
        if(userNotifications != null) {
            userNotifications.stream()
                    .sorted()
                    .forEach(userNotification -> userNotification.setTimeDifferenceFromNow(DateUtility.setTimeDifferenceFromNow(userNotification.getCreateDate())));
        }
        return userNotifications;
    }

    @Override
    public Boolean getUserNotificationStatus(User user) {
        Boolean notificationsStatus = usersNotificationStatus.get(user.getUsername());
        return notificationsStatus;
    }

    @Override
    public void setReadedNotificationStatus(User user) {
        usersNotificationStatus.put(user.getUsername(), false);
    }

    @Override
    public void removeOldestNotification(List<NotificationDTO> userNotificationList) {
        if(userNotificationList.size() > MAX_UNREADED_NOTIFICATIONS){
            userNotificationList.remove(userNotificationList.size()-1);
        }
    }


}
