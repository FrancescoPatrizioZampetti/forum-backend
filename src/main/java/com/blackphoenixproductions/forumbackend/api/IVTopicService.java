package com.blackphoenixproductions.forumbackend.api;

import com.blackphoenixproductions.forumbackend.dto.Filter;
import com.blackphoenixproductions.forumbackend.entity.VTopic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IVTopicService {
    Page<VTopic>  getPagedTopics (Pageable pageable, Filter filter);
}
