package com.blackphoenixproductions.forumbackend.infrastructure.api.assembler;

import com.blackphoenixproductions.forumbackend.infrastructure.api.TopicRestAPIController;
import com.blackphoenixproductions.forumbackend.domain.entity.VTopic;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VTopicAssembler implements RepresentationModelAssembler<VTopic, EntityModel<VTopic>> {
    @Override
    public EntityModel<VTopic> toModel(VTopic entity) {
        return EntityModel.of(entity, linkTo(methodOn(TopicRestAPIController.class).findTopic(entity.getId())).withSelfRel());
//                linkTo(methodOn(TopicRestAPIController.class).findTopic(entity.getId())).withRel(""));
    }
}
