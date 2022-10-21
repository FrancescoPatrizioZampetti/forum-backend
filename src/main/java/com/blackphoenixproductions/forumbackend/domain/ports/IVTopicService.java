package com.blackphoenixproductions.forumbackend.domain.ports;

import com.blackphoenixproductions.forumbackend.adapters.api.dto.Filter;
import com.blackphoenixproductions.forumbackend.domain.model.VTopic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IVTopicService {
    Page<VTopic>  getPagedTopics (Pageable pageable, Filter filter);
}
