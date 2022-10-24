package com.blackphoenixproductions.forumbackend.domain.repository;

import com.blackphoenixproductions.forumbackend.domain.entity.Notification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificationRepository extends CrudRepository<Notification, String> {

    List<Notification> findAllByToUserOrderByCreateDateDesc(String username);
}
