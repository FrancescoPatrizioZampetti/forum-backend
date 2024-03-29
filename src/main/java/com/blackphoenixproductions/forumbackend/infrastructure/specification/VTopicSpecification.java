package com.blackphoenixproductions.forumbackend.infrastructure.specification;

import com.blackphoenixproductions.forumbackend.domain.entity.VTopic;
import com.blackphoenixproductions.forumbackend.domain.entity.VTopic_;
import org.springframework.data.jpa.domain.Specification;


public class VTopicSpecification {

    public static Specification<VTopic> topicAuthor(String name){
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get(VTopic_.AUTHOR_USERNAME), name);
    }

}
