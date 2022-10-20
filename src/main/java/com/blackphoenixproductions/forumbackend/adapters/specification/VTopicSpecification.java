package com.blackphoenixproductions.forumbackend.adapters.specification;

import com.blackphoenixproductions.forumbackend.domain.model.VTopic;
import com.blackphoenixproductions.forumbackend.domain.model.VTopic_;
import org.springframework.data.jpa.domain.Specification;


public class VTopicSpecification {

    public static Specification<VTopic> topicAuthor(String name){
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get(VTopic_.AUTHOR_USERNAME), name);
    }

}
