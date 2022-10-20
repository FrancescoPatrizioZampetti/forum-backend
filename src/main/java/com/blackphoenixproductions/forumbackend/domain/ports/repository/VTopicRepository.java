package com.blackphoenixproductions.forumbackend.domain.ports.repository;


import com.blackphoenixproductions.forumbackend.domain.model.VTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VTopicRepository extends JpaRepository<VTopic, Long>, JpaSpecificationExecutor<VTopic> {
}
