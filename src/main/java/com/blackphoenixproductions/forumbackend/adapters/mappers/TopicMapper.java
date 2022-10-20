package com.blackphoenixproductions.forumbackend.adapters.mappers;

import com.blackphoenixproductions.forumbackend.adapters.dto.topic.EditTopicDTO;
import com.blackphoenixproductions.forumbackend.adapters.dto.topic.InsertTopicDTO;
import com.blackphoenixproductions.forumbackend.domain.model.Topic;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TopicMapper {

    Topic insertTopicDTOtoTopic(InsertTopicDTO insertTopicDTO);
    Topic editTopicDTOtoTopic(EditTopicDTO editTopicDTO);
}
