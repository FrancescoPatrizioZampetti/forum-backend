package com.blackphoenixproductions.forumbackend.repository;

import com.blackphoenixproductions.forumbackend.dto.NotificationDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationDTO, Long> {

    List<NotificationDTO> findAllByFromUser(String username);
}
