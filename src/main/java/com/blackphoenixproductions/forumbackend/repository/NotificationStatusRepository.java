package com.blackphoenixproductions.forumbackend.repository;

import com.blackphoenixproductions.forumbackend.dto.NotificationStatusDTO;
import org.springframework.data.repository.CrudRepository;

public interface NotificationStatusRepository extends CrudRepository<NotificationStatusDTO, String> {
}
