package com.blackphoenixproductions.forumbackend.adapters.api;

import com.blackphoenixproductions.forumbackend.adapters.api.assembler.PostAssembler;
import com.blackphoenixproductions.forumbackend.domain.ports.INotificationService;
import com.blackphoenixproductions.forumbackend.domain.ports.IPostService;
import com.blackphoenixproductions.forumbackend.domain.dto.openApi.post.EditPostDTO;
import com.blackphoenixproductions.forumbackend.domain.dto.openApi.post.InsertPostDTO;
import com.blackphoenixproductions.forumbackend.domain.model.Post;
import com.blackphoenixproductions.forumbackend.config.security.KeycloakUtility;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
@Tag(name = "3. Post", description = "endpoints riguardanti i post.")
public class PostRestAPIController {

    private static final Logger logger = LoggerFactory.getLogger(PostRestAPIController.class);

    private final IPostService postService;
    private final INotificationService notificationService;
    private final PostAssembler postDTOAssembler;

    @Autowired
    public PostRestAPIController(IPostService postService, INotificationService notificationService, PostAssembler postDTOAssembler) {
        this.postService = postService;
        this.notificationService = notificationService;
        this.postDTOAssembler = postDTOAssembler;
    }


    @Operation(summary = "Restituisce il numero totale dei post.")
    @GetMapping(value = "/getTotalPosts")
    public ResponseEntity<Long> getTotalPosts (HttpServletRequest req){
        Long totalPosts = postService.getTotalPosts();
        return new ResponseEntity<Long>(totalPosts, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Forbidden.", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "Cerca tutti i post contenuti in una pagina di un topic.")
    @PostMapping(value = "/findPostsByPage")
    public ResponseEntity<PagedModel<EntityModel<Post>>> findPostsByPage (@Parameter(description = "L'id del topic.") @RequestParam Long topicId,
                                                                          @ParameterObject @PageableDefault(sort = {"createDate"}, direction = Sort.Direction.ASC) Pageable pageable,
                                                                          PagedResourcesAssembler<Post> pagedResourcesAssembler){
        Page<Post> pagedPosts = postService.getPagedPosts(topicId, pageable);
        PagedModel<EntityModel<Post>> pagedModel = pagedResourcesAssembler.toModel(pagedPosts, postDTOAssembler);
        return new ResponseEntity<PagedModel<EntityModel<Post>>> (pagedModel, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Forbidden.", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "Crea un nuovo post.")
    @PostMapping(value = "createPost")
    public ResponseEntity<EntityModel<Post>> createPost(@RequestBody @Valid InsertPostDTO postDTO, HttpServletRequest req){
        logger.info("Start createPost - post owner username : {}", KeycloakUtility.getAccessToken(req).getPreferredUsername());
        Post savedPost = postService.createPost(postDTO, req);
        notificationService.notifyTopicAuthor(savedPost);
        EntityModel<Post> entityModel = EntityModel.of(savedPost, linkTo(methodOn(PostRestAPIController.class).createPost(postDTO, req)).withSelfRel());
        logger.info("End createPost");
        return new ResponseEntity<EntityModel<Post>>(entityModel, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request: post non esistente.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Forbidden.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthorized.", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "Modifica un post.")
    @PostMapping(value = "editPost")
    public ResponseEntity<EntityModel<Post>> editPost(@RequestBody @Valid EditPostDTO postDTO, HttpServletRequest req){
        logger.info("Start editPost - post id : {}", postDTO.getId());
        Post editedPost = postService.editPost(postDTO, req);
        EntityModel<Post> entityModel = EntityModel.of(editedPost, linkTo(methodOn(PostRestAPIController.class).editPost(postDTO, req)).withSelfRel());
        logger.info("End editPost");
        return new ResponseEntity<EntityModel<Post>>(entityModel, HttpStatus.OK);
    }

}


