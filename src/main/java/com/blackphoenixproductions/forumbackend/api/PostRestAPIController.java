package com.blackphoenixproductions.forumbackend.api;

import com.blackphoenixproductions.forumbackend.assembler.PostDTOAssembler;
import com.blackphoenixproductions.forumbackend.service.INotificationService;
import com.blackphoenixproductions.forumbackend.service.IPostService;
import dto.PostDTO;
import dto.openApi.post.EditPostDTO;
import dto.openApi.post.InsertPostDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
@Tag(name = "3. Post", description = "endpoints riguardanti i post.")
public class PostRestAPIController {

    private static final Logger logger = LoggerFactory.getLogger(PostRestAPIController.class);

    private final IPostService postService;
    private final INotificationService notificationService;
    private final PostDTOAssembler postDTOAssembler;

    @Autowired
    public PostRestAPIController(IPostService postService, INotificationService notificationService, PostDTOAssembler postDTOAssembler) {
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
    @Operation(summary = "Cerca tutti i post contenuti in una pagina di un topic.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/findPostsByPage")
    public ResponseEntity<PagedModel<EntityModel<PostDTO>>> findPostsByPage (@RequestParam Long topicId,
                                                                             @ParameterObject @PageableDefault(sort = {"createDate"}, direction = Sort.Direction.ASC) Pageable pageable,
                                                                             PagedResourcesAssembler<PostDTO> pagedResourcesAssembler){
        Page<PostDTO> pagedPosts = postService.getPagedPosts(topicId, pageable);
        PagedModel<EntityModel<PostDTO>> pagedModel = pagedResourcesAssembler.toModel(pagedPosts, postDTOAssembler);
        return new ResponseEntity<PagedModel<EntityModel<PostDTO>>> (pagedModel, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Forbidden.", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "Crea un nuovo post.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "createPost")
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK') or hasRole('ROLE_GOOGLE')")
    public ResponseEntity<EntityModel<PostDTO>> createPost(@RequestBody InsertPostDTO postDTO){
        logger.info("Start createPost - post owner username : {}", postDTO.getUsername());
        PostDTO savedPost = postService.createPost(postDTO);
        notificationService.notifyTopicAuthor(savedPost);
        EntityModel<PostDTO> entityModel = EntityModel.of(savedPost, linkTo(methodOn(PostRestAPIController.class).createPost(postDTO)).withSelfRel());
        logger.info("End createPost");
        return new ResponseEntity<EntityModel<PostDTO>>(entityModel, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request: post non esistente.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Forbidden.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthorized.", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "Modifica un post.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "editPost")
    @PreAuthorize("hasRole('ROLE_STAFF') or hasRole('ROLE_USER') or hasRole('ROLE_FACEBOOK') or hasRole('ROLE_GOOGLE')")
    public ResponseEntity<EntityModel<PostDTO>> editPost(@RequestBody EditPostDTO postDTO, HttpServletRequest req){
        logger.info("Start editPost - post id : {}", postDTO.getId());
        PostDTO editedPost = postService.editPost(postDTO, req);
        EntityModel<PostDTO> entityModel = EntityModel.of(editedPost, linkTo(methodOn(PostRestAPIController.class).editPost(postDTO, req)).withSelfRel());
        logger.info("End editPost");
        return new ResponseEntity<EntityModel<PostDTO>>(entityModel, HttpStatus.OK);
    }

}


