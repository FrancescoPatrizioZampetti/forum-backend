package com.blackphoenixproductions.forumbackend.adapters.mappers;

import com.blackphoenixproductions.forumbackend.adapters.api.dto.post.EditPostDTO;
import com.blackphoenixproductions.forumbackend.adapters.api.dto.post.InsertPostDTO;
import com.blackphoenixproductions.forumbackend.domain.model.Post;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PostMapper {

    @Mapping(target = "topic.id", source = "topicId")
    Post insertPostDTOtoPost(InsertPostDTO insertPostDTO);
    Post editPostDTOtoPost(EditPostDTO editPostDTO);


}
