package com.blackphoenixproductions.forumbackend.domain.ports.repository;

import com.blackphoenixproductions.forumbackend.domain.dto.NotificationDTO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificationRepository extends CrudRepository<NotificationDTO, String> {

    List<NotificationDTO> findAllByToUserOrderByCreateDateDesc(String username);
}
