package com.blackphoenixproductions.forumbackend.assembler;

import com.blackphoenixproductions.forumbackend.api.PostRestAPIController;
import dto.PostDTO;
import dto.openApi.post.EditPostDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PostDTOAssembler implements RepresentationModelAssembler<PostDTO, EntityModel<PostDTO>> {
    @Override
    public EntityModel<PostDTO> toModel(PostDTO entity) {
        return EntityModel.of(entity, linkTo(methodOn(PostRestAPIController.class).editPost(new EditPostDTO(entity.getId()), null)).withSelfRel());
    }
}
