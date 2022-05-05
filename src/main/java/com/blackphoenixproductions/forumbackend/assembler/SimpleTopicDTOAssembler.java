package com.blackphoenixproductions.forumbackend.assembler;

import com.blackphoenixproductions.forumbackend.api.TopicRestAPIController;
import dto.SimpleTopicDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SimpleTopicDTOAssembler implements RepresentationModelAssembler<SimpleTopicDTO, EntityModel<SimpleTopicDTO>> {
    @Override
    public EntityModel<SimpleTopicDTO> toModel(SimpleTopicDTO entity) {
        return EntityModel.of(entity, linkTo(methodOn(TopicRestAPIController.class).findTopic(entity.getId())).withSelfRel());
//                linkTo(methodOn(TopicRestAPIController.class).findTopic(entity.getId())).withRel(""));
    }
}
