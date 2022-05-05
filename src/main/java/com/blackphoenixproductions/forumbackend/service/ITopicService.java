package com.blackphoenixproductions.forumbackend.service;


import dto.SimpleTopicDTO;
import dto.TopicDTO;
import dto.openApi.topic.EditTopicDTO;
import dto.openApi.topic.InsertTopicDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;

public interface ITopicService {

    Long getTotalTopics();

    Page<SimpleTopicDTO> getPagedTopics(Pageable pageable, String title, String username);

    SimpleTopicDTO createTopic(InsertTopicDTO insertTopicDTO);

    TopicDTO editTopic(EditTopicDTO topicDTO, HttpServletRequest req);

    TopicDTO getTopic (Long id);
}
