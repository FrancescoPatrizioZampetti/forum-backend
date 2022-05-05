package com.blackphoenixproductions.forumbackend.assembler;

import com.blackphoenixproductions.forumbackend.api.TopicRestAPIController;
import com.blackphoenixproductions.forumbackend.entity.Topic;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TopicAssembler implements RepresentationModelAssembler<Topic, EntityModel<Topic>> {
    @Override
    public EntityModel<Topic> toModel(Topic entity) {
        return EntityModel.of(entity, linkTo(methodOn(TopicRestAPIController.class).findTopic(entity.getId())).withSelfRel());
//                linkTo(methodOn(TopicRestAPIController.class).findTopic(entity.getId())).withRel(""));
    }
}
