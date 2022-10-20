package com.blackphoenixproductions.forumbackend.service.repository;

import com.blackphoenixproductions.forumbackend.entity.Post;
import com.blackphoenixproductions.forumbackend.entity.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByTopicAndDeleteDateIsNull(Topic topic, Pageable pageable);
    Optional<Post> findById(Long id);

}
