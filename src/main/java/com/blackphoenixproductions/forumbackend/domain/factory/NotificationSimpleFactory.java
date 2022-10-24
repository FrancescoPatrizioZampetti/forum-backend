package com.blackphoenixproductions.forumbackend.domain.factory;

import com.blackphoenixproductions.forumbackend.domain.entity.Notification;
import com.blackphoenixproductions.forumbackend.domain.entity.Post;
import com.blackphoenixproductions.forumbackend.domain.enums.Pagination;
import com.blackphoenixproductions.forumbackend.domain.ports.IPostService;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NotificationSimpleFactory {

    private static final int MAX_NOTIFICATION_LENGTH = 20;
    private final IPostService postService;

    @Autowired
    public NotificationSimpleFactory(IPostService postService) {
        this.postService = postService;
    }

    public Notification createUserNotification(Post post){
        Notification notification = createNotification(post);
        int lastTopicPageNumber = postService.getPagedPosts(post.getTopic().getId(), PageRequest.of(0, Math.toIntExact(Pagination.POST_PAGINATION.getValue()), Sort.by("createDate").ascending())).getTotalPages()-1;
        notification.setUrl("/viewtopic?id=" + post.getTopic().getId() + "&page=" + lastTopicPageNumber);
        return notification;
    }

    private Notification createNotification(Post post){
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
}
