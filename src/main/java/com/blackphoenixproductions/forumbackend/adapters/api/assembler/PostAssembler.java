package com.blackphoenixproductions.forumbackend.adapters.api.assembler;

import com.blackphoenixproductions.forumbackend.adapters.api.PostRestAPIController;
import com.blackphoenixproductions.forumbackend.adapters.api.dto.post.EditPostDTO;
import com.blackphoenixproductions.forumbackend.domain.model.Post;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PostAssembler implements RepresentationModelAssembler<Post, EntityModel<Post>> {
    @Override
    public EntityModel<Post> toModel(Post entity) {
        return EntityModel.of(entity, linkTo(methodOn(PostRestAPIController.class).editPost(new EditPostDTO(entity.getId()), null)).withSelfRel());
    }
}
