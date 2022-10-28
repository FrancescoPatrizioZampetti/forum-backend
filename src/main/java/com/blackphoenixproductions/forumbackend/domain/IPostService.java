package com.blackphoenixproductions.forumbackend.domain;


import com.blackphoenixproductions.forumbackend.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface IPostService {

    Long getTotalPosts();

    Page<Post> getPagedPosts(Long topicId, Pageable pageable);

    Post createPost(Post post, String email);

    Post editPost(Post post, String email, Set<String> roles);
}
