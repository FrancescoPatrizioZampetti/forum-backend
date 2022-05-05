package com.blackphoenixproductions.forumbackend.mapping;

import com.blackphoenixproductions.forumbackend.entity.Topic;
import dto.SimpleTopicDTO;
import dto.TopicDTO;
import dto.openApi.topic.InsertTopicDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TopicMapper {

    TopicDTO topicToTopicDTO(Topic topic);
    Topic topicDTOtoTopic(TopicDTO topicDTO);
    Topic insertTopicDTOtoTopic(InsertTopicDTO insertTopicDTO);
    SimpleTopicDTO topicToSimpleTopicDTO(Topic topic);

}
