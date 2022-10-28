package com.blackphoenixproductions.forumbackend.domain;

import com.blackphoenixproductions.forumbackend.domain.entity.Post;
import com.blackphoenixproductions.forumbackend.domain.entity.User;

public interface IEmailSender {

    void sendTopicReplyEmail(User user, User userReply, Post post);
}
