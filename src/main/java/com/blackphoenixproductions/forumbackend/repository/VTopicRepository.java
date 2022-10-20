package com.blackphoenixproductions.forumbackend.repository;


import com.blackphoenixproductions.forumbackend.entity.VTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VTopicRepository extends JpaRepository<VTopic, Long>, JpaSpecificationExecutor<VTopic> {
}
