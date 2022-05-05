package com.blackphoenixproductions.forumbackend.mapping;

import com.blackphoenixproductions.forumbackend.entity.Post;
import dto.PostDTO;
import dto.openApi.post.InsertPostDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PostMapper {

    PostDTO postToPostDTO(Post post);
    Post postDTOtoPost(PostDTO postDTO);
    Post insertPostDTOtoPost(InsertPostDTO insertPostDTO);

}
