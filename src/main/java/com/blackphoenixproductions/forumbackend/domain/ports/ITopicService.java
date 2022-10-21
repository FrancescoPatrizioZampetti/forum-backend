package com.blackphoenixproductions.forumbackend.domain.ports;

import com.blackphoenixproductions.forumbackend.domain.model.Topic;

import java.util.Set;

public interface ITopicService {

    Long getTotalTopics();

    Topic editTopic(Topic topicDTO, Set<String> roles);

    Topic getTopic(Long id);

    Topic createTopic(Topic topic, String email);
}
