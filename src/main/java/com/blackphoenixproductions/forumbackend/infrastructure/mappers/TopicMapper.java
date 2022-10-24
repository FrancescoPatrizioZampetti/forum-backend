package com.blackphoenixproductions.forumbackend.infrastructure.mappers;

import com.blackphoenixproductions.forumbackend.infrastructure.api.dto.topic.EditTopicDTO;
import com.blackphoenixproductions.forumbackend.infrastructure.api.dto.topic.InsertTopicDTO;
import com.blackphoenixproductions.forumbackend.domain.entity.Topic;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TopicMapper {

    Topic insertTopicDTOtoTopic(InsertTopicDTO insertTopicDTO);
    Topic editTopicDTOtoTopic(EditTopicDTO editTopicDTO);

}
