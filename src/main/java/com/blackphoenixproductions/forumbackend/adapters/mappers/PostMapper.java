package com.blackphoenixproductions.forumbackend.adapters.mappers;

import com.blackphoenixproductions.forumbackend.adapters.dto.post.EditPostDTO;
import com.blackphoenixproductions.forumbackend.adapters.dto.post.InsertPostDTO;
import com.blackphoenixproductions.forumbackend.domain.model.Post;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PostMapper {

    Post insertPostDTOtoPost(InsertPostDTO insertPostDTO);
    Post editPostDTOtoPost(EditPostDTO editPostDTO);

}
