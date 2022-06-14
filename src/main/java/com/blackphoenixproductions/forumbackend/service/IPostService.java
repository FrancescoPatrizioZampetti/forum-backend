package com.blackphoenixproductions.forumbackend.service;


import com.blackphoenixproductions.forumbackend.dto.openApi.post.EditPostDTO;
import com.blackphoenixproductions.forumbackend.dto.openApi.post.InsertPostDTO;
import com.blackphoenixproductions.forumbackend.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;

public interface IPostService {

    Long getTotalPosts();

    Page<Post> getPagedPosts(Long topicId, Pageable pageable);

    Post createPost(InsertPostDTO postDTO, HttpServletRequest req);

    Post editPost(EditPostDTO postDTO, HttpServletRequest req);

}
