package com.blackphoenixproductions.forumbackend.domain.ports;

public interface MessagePublisher {
    void publish(final String message);
}
