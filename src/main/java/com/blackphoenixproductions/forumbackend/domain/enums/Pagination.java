package com.blackphoenixproductions.forumbackend.domain.enums;

public enum Pagination {

    TOPIC_PAGINATION (3L),
    POST_PAGINATION (10L);

    private final Long value;

    Pagination(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }
}
