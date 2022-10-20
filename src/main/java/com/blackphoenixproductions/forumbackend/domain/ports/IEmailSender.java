package com.blackphoenixproductions.forumbackend.domain.ports;

import com.blackphoenixproductions.forumbackend.domain.model.Post;
import com.blackphoenixproductions.forumbackend.domain.model.User;

public interface IEmailSender {

    void sendTopicReplyEmail(User user, User userReply, Post post);
}
