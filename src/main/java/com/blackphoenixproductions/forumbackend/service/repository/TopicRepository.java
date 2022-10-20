package com.blackphoenixproductions.forumbackend.service.repository;

import com.blackphoenixproductions.forumbackend.entity.Topic;
import com.blackphoenixproductions.forumbackend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    Page<Topic> findByDeleteDateIsNullAndTitleIgnoreCaseContainingAndUser(Pageable pageable, String title, User user);
    Page<Topic> findByDeleteDateIsNullAndTitleIgnoreCaseContaining(Pageable pageable, String title);
    Optional<Topic> findByIdAndDeleteDateIsNull(Long id);

}