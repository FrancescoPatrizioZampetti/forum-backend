package com.blackphoenixproductions.forumbackend.service.repository;

import com.blackphoenixproductions.forumbackend.dto.NotificationDTO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificationRepository extends CrudRepository<NotificationDTO, String> {

    List<NotificationDTO> findAllByToUserOrderByCreateDateDesc(String username);
}
