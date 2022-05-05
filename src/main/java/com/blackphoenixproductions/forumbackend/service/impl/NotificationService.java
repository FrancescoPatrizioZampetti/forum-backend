package com.blackphoenixproductions.forumbackend.service.impl;

import com.blackphoenixproductions.forumbackend.service.INotificationService;
import com.blackphoenixproductions.forumbackend.service.IPostService;
import com.blackphoenixproductions.forumbackend.sse.SsePushNotificationService;
import com.blackphoenixproductions.forumbackend.utility.DateUtility;
import dto.NotificationDTO;
import dto.PostDTO;
import dto.SimpleUserDTO;
import enums.Pagination;
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
    public void notifyTopicAuthor(PostDTO postDTO) {
        if (userIsNotTopicAuthor(postDTO)){
            String topicAuthorUsername = postDTO.getTopic().getUser().getUsername();
            NotificationDTO notification = createUserNotification(postDTO);
            List<NotificationDTO> userNotificationList = addToUserNotifications(topicAuthorUsername, notification);
            removeOldestNotification(userNotificationList);
            updateStatusAndstoreUserNotification(topicAuthorUsername, userNotificationList);
            // invio sse event per notifica push
            ssePushNotificationService.sendNotificationToTopicAuthor(topicAuthorUsername);
        }
    }

    private boolean userIsNotTopicAuthor(PostDTO postDTO) {
        return !postDTO.getUser().equals(postDTO.getTopic().getUser());
    }

    private NotificationDTO createUserNotification(PostDTO postDTO) {
        NotificationDTO notification = new NotificationDTO();
        notification.setId(idCounter.getAndIncrement());
        notification.setFromUser(postDTO.getUser());
        notification.setToUser(postDTO.getTopic().getUser());
        notification.setTopic(postDTO.getTopic());
        notification.setCreateDate(new Date());
        notification.setTimeDifferenceFromNow(DateUtility.setTimeDifferenceFromNow(notification.getCreateDate()));
        notification.setMessage(setNotificationMessage(postDTO.getMessage()));
        int lastTopicPageNumber = postService.getPagedPosts(postDTO.getTopic().getId(), PageRequest.of(0, Math.toIntExact(Pagination.POST_PAGINATION.getValue()), Sort.by("createDate").ascending())).getTotalPages()-1;
        notification.setUrl("/viewtopic?id=" + postDTO.getTopic().getId() + "&page=" + lastTopicPageNumber);
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
    public List<NotificationDTO> getUserNotification(SimpleUserDTO simpleUserDTO) {
        List<NotificationDTO> userNotifications = notificationStore.get(simpleUserDTO.getUsername());
        if(userNotifications != null) {
            userNotifications.stream()
                    .sorted()
                    .forEach(userNotification -> userNotification.setTimeDifferenceFromNow(DateUtility.setTimeDifferenceFromNow(userNotification.getCreateDate())));
        }
        return userNotifications;
    }

    @Override
    public Boolean getUserNotificationStatus(SimpleUserDTO simpleUserDTO) {
        Boolean notificationsStatus = usersNotificationStatus.get(simpleUserDTO.getUsername());
        return notificationsStatus;
    }

    @Override
    public void setReadedNotificationStatus(SimpleUserDTO simpleUserDTO) {
        usersNotificationStatus.put(simpleUserDTO.getUsername(), false);
    }

    @Override
    public void removeOldestNotification(List<NotificationDTO> userNotificationList) {
        if(userNotificationList.size() > MAX_UNREADED_NOTIFICATIONS){
            userNotificationList.remove(userNotificationList.size()-1);
        }
    }


}
