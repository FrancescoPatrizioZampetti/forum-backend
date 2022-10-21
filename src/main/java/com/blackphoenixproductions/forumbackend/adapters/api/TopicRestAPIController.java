package com.blackphoenixproductions.forumbackend.adapters.api;

import com.blackphoenixproductions.forumbackend.adapters.api.assembler.VTopicAssembler;
import com.blackphoenixproductions.forumbackend.adapters.mappers.TopicMapper;
import com.blackphoenixproductions.forumbackend.domain.ports.ITopicService;
import com.blackphoenixproductions.forumbackend.adapters.api.dto.Filter;
import com.blackphoenixproductions.forumbackend.adapters.api.dto.topic.EditTopicDTO;
import com.blackphoenixproductions.forumbackend.adapters.api.dto.topic.InsertTopicDTO;
import com.blackphoenixproductions.forumbackend.domain.model.Topic;
import com.blackphoenixproductions.forumbackend.domain.model.VTopic;
import com.blackphoenixproductions.forumbackend.config.security.KeycloakUtility;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.keycloak.representations.AccessToken;
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
@Tag(name = "2. Topic", description = "endpoints riguardanti i topic.")
public class TopicRestAPIController {

    private static final Logger logger = LoggerFactory.getLogger(TopicRestAPIController.class);
    private final ITopicService topicService;
    private final VTopicAssembler vTopicAssembler;
    private final TopicMapper topicMapper;

    @Autowired
    public TopicRestAPIController(ITopicService topicService, VTopicAssembler vTopicAssembler, TopicMapper topicMapper) {
        this.topicService = topicService;
        this.vTopicAssembler = vTopicAssembler;
        this.topicMapper = topicMapper;
    }


    @Operation(summary = "Restituisce il numero totale dei topic.")
    @GetMapping(value = "/getTotalTopics")
    public ResponseEntity<Long> getTotalTopics (HttpServletRequest req){
        Long totalTopics = topicService.getTotalTopics();
        return new ResponseEntity<Long>(totalTopics, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "Ricerca di un topic con paginazione e filtro dinamico.")
    @PostMapping(value = "/findFilteredTopicsByPage")
    public ResponseEntity<PagedModel<EntityModel<VTopic>>> findFilteredTopicsByPage (@ParameterObject @PageableDefault(sort = {"createDate"}, direction = Sort.Direction.DESC) Pageable pageable,
                                                                                     @RequestBody (required = false) Filter filter,
                                                                                     PagedResourcesAssembler<VTopic> pagedResourcesAssembler){
        Page<VTopic> pagedTopics = topicService.getPagedTopics(pageable, filter);
        PagedModel<EntityModel<VTopic>> pagedModel = pagedResourcesAssembler.toModel(pagedTopics, vTopicAssembler);
        return new ResponseEntity<PagedModel<EntityModel<VTopic>>>  (pagedModel, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request: topic non trovato.", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "Ricerca di un topic.")
    @GetMapping(value = "/findTopic")
    public ResponseEntity<EntityModel<Topic>> findTopic (@Parameter(description = "L'id del topic da cercare.") @RequestParam Long id){
        Topic topic = topicService.getTopic(id);
        EntityModel<Topic> entityModel = EntityModel.of(topic).add(linkTo(methodOn(TopicRestAPIController.class).findTopic(id)).withSelfRel());
        return new ResponseEntity<EntityModel<Topic>> (entityModel, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Forbidden.", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "Creazione di un topic.")
    @PostMapping(value = "createTopic")
    public ResponseEntity<EntityModel<Topic>> createTopic(@RequestBody @Valid InsertTopicDTO insertTopicDTO, HttpServletRequest req){
        logger.info("Start createTopic - topic owner username : {}", KeycloakUtility.getAccessToken(req).getPreferredUsername());
        AccessToken accessToken = KeycloakUtility.getAccessToken(req);
        Topic savedTopic = topicService.createTopic(topicMapper.insertTopicDTOtoTopic(insertTopicDTO), accessToken.getEmail());
        EntityModel<Topic> entityModel = EntityModel.of(savedTopic).add(linkTo(methodOn(TopicRestAPIController.class).createTopic(insertTopicDTO, req)).withSelfRel());
        logger.info("End createTopic");
        return new ResponseEntity<EntityModel<Topic>>(entityModel, HttpStatus.OK);
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request: topic non trovato.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Forbidden.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthorized.", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "Modifica di un topic.")
    @PostMapping(value = "editTopic")
    public ResponseEntity<EntityModel<Topic>> editTopic(@RequestBody @Valid EditTopicDTO topicDTO, HttpServletRequest req){
        logger.info("Start editTopic - topic id: {}", topicDTO.getId());
        AccessToken accessToken = KeycloakUtility.getAccessToken(req);
        Topic editedTopic = topicService.editTopic(topicMapper.editTopicDTOtoTopic(topicDTO), KeycloakUtility.getRoles(accessToken));
        EntityModel<Topic> entityModel = EntityModel.of(editedTopic).add(linkTo(methodOn(TopicRestAPIController.class).editTopic(topicDTO, req)).withSelfRel());
        logger.info("End editTopic");
        return new ResponseEntity<EntityModel<Topic>>(entityModel, HttpStatus.OK);
    }

}
