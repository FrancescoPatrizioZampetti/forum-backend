package com.blackphoenixproductions.forumbackend.messagebroker;

public interface MessagePublisher {
    void publish(final String message);
}
