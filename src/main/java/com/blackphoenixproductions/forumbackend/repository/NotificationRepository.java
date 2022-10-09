package com.blackphoenixproductions.forumbackend.repository;

import com.blackphoenixproductions.forumbackend.dto.NotificationDTO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificationRepository extends CrudRepository<NotificationDTO, String> {

    List<NotificationDTO> findAllByToUserOrderByCreateDateAsc(String username);
}
