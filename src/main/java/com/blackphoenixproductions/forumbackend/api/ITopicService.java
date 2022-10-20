package com.blackphoenixproductions.forumbackend.api;

import com.blackphoenixproductions.forumbackend.dto.openApi.topic.EditTopicDTO;
import com.blackphoenixproductions.forumbackend.dto.openApi.topic.InsertTopicDTO;
import com.blackphoenixproductions.forumbackend.entity.Topic;

import javax.servlet.http.HttpServletRequest;

public interface ITopicService {

    Long getTotalTopics();

    Topic createTopic(InsertTopicDTO insertTopicDTO, HttpServletRequest req);

    Topic editTopic(EditTopicDTO topicDTO, HttpServletRequest req);

    Topic getTopic(Long id);
}
