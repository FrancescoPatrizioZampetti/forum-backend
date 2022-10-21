package com.blackphoenixproductions.forumbackend.domain.ports;

import com.blackphoenixproductions.forumbackend.adapters.api.dto.Filter;
import com.blackphoenixproductions.forumbackend.domain.entity.Topic;
import com.blackphoenixproductions.forumbackend.domain.entity.VTopic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface ITopicService {

    Long getTotalTopics();

    Topic editTopic(Topic topicDTO, Set<String> roles);

    Topic getTopic(Long id);

    Topic createTopic(Topic topic, String email);

    Page<VTopic> getPagedTopics(Pageable pageable, Filter filter);
}
