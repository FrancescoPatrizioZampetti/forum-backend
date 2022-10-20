package com.blackphoenixproductions.forumbackend.domain.ports;


import com.blackphoenixproductions.forumbackend.domain.dto.openApi.post.EditPostDTO;
import com.blackphoenixproductions.forumbackend.domain.dto.openApi.post.InsertPostDTO;
import com.blackphoenixproductions.forumbackend.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;

public interface IPostService {

    Long getTotalPosts();

    Page<Post> getPagedPosts(Long topicId, Pageable pageable);

    Post createPost(InsertPostDTO postDTO, HttpServletRequest req);

    Post editPost(EditPostDTO postDTO, HttpServletRequest req);

}
