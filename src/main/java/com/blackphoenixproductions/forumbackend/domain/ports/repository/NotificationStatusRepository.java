package com.blackphoenixproductions.forumbackend.domain.ports.repository;

import com.blackphoenixproductions.forumbackend.domain.dto.NotificationStatusDTO;
import org.springframework.data.repository.CrudRepository;

public interface NotificationStatusRepository extends CrudRepository<NotificationStatusDTO, String> {
}
