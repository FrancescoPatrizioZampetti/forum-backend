package com.blackphoenixproductions.forumbackend.infrastructure.mappers;

import com.blackphoenixproductions.forumbackend.infrastructure.api.dto.post.EditPostDTO;
import com.blackphoenixproductions.forumbackend.infrastructure.api.dto.post.InsertPostDTO;
import com.blackphoenixproductions.forumbackend.domain.entity.Post;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PostMapper {

    @Mapping(target = "topic.id", source = "topicId")
    Post insertPostDTOtoPost(InsertPostDTO insertPostDTO);
    Post editPostDTOtoPost(EditPostDTO editPostDTO);


}
