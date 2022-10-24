package com.blackphoenixproductions.forumbackend.domain;

public interface MessagePublisher {
    void publish(final String message);
}
