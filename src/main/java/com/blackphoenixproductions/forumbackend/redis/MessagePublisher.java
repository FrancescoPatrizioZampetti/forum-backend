package com.blackphoenixproductions.forumbackend.redis;

public interface MessagePublisher {
    void publish(final String message);
}
