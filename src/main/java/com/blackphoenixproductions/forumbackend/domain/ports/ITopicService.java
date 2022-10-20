package com.blackphoenixproductions.forumbackend.domain.ports;

import com.blackphoenixproductions.forumbackend.adapters.dto.topic.EditTopicDTO;
import com.blackphoenixproductions.forumbackend.adapters.dto.topic.InsertTopicDTO;
import com.blackphoenixproductions.forumbackend.domain.model.Topic;

import javax.servlet.http.HttpServletRequest;

public interface ITopicService {

    Long getTotalTopics();

    Topic createTopic(InsertTopicDTO insertTopicDTO, HttpServletRequest req);

    Topic editTopic(EditTopicDTO topicDTO, HttpServletRequest req);

    Topic getTopic(Long id);
}
