package com.blackphoenixproductions.forumbackend.service;

import dto.PostDTO;
import dto.openApi.post.EditPostDTO;
import dto.openApi.post.InsertPostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;

public interface IPostService {

    Long getTotalPosts();

    Page<PostDTO> getPagedPosts(Long topicId, Pageable pageable);

    PostDTO createPost(InsertPostDTO postDTO);

    PostDTO editPost(EditPostDTO postDTO, HttpServletRequest req);

}
