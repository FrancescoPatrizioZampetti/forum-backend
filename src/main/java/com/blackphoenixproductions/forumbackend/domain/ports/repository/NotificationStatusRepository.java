package com.blackphoenixproductions.forumbackend.domain.ports.repository;

import com.blackphoenixproductions.forumbackend.domain.model.NotificationStatus;
import org.springframework.data.repository.CrudRepository;

public interface NotificationStatusRepository extends CrudRepository<NotificationStatus, String> {
}
