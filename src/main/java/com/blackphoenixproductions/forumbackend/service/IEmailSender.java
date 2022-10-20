package com.blackphoenixproductions.forumbackend.service;

import com.blackphoenixproductions.forumbackend.entity.Post;
import com.blackphoenixproductions.forumbackend.entity.User;

public interface IEmailSender {

    void sendTopicReplyEmail(User user, User userReply, Post post);
}
