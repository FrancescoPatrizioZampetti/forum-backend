package com.blackphoenixproductions.forumbackend.repository;

import com.blackphoenixproductions.forumbackend.dto.NotificationStatusDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationStatusRepository extends CrudRepository<NotificationStatusDTO, String> {
}
