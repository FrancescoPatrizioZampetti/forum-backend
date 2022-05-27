package com.blackphoenixproductions.forumbackend.api;

import com.blackphoenixproductions.forumbackend.assembler.VTopicAssembler;
import com.blackphoenixproductions.forumbackend.dto.Filter;
import com.blackphoenixproductions.forumbackend.dto.KeyValueDTO;
import com.blackphoenixproductions.forumbackend.dto.openApi.topic.EditTopicDTO;
import com.blackphoenixproductions.forumbackend.dto.openApi.topic.InsertTopicDTO;
import com.blackphoenixproductions.forumbackend.entity.Topic;
import com.blackphoenixproductions.forumbackend.entity.VTopic;
import com.blackphoenixproductions.forumbackend.security.KeycloakUtility;
import com.blackphoenixproductions.forumbackend.service.ITopicService;
import com.blackphoenixproductions.forumbackend.service.impl.VTopicService;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
@Tag(name = "2. Topic", description = "endpoints riguardanti i topic.")
public class TopicRestAPIController {

    private static final Logger logger = LoggerFactory.getLogger(TopicRestAPIController.class);
    private final ITopicService topicService;
    private final VTopicAssembler vTopicAssembler;
    private final VTopicService vTopicService;

    @Autowired
    public TopicRestAPIController(ITopicService topicService, VTopicAssembler vTopicAssembler, VTopicService vTopicService) {
        this.topicService = topicService;
        this.vTopicAssembler = vTopicAssembler;
        this.vTopicService = vTopicService;
    }


    @Operation(summary = "Restituisce il numero totale dei topic.")
    @GetMapping(value = "/getTotalTopics")
    public ResponseEntity<KeyValueDTO> getTotalTopics (HttpServletRequest req){
        Long totalTopics = topicService.getTotalTopics();
        return new ResponseEntity<KeyValueDTO>(new KeyValueDTO("totalTopics", totalTopics), HttpStatus.OK);
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
        Page<VTopic> pagedTopics = vTopicService.getPagedTopics(pageable, filter);
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
    public ResponseEntity<EntityModel<Topic>> createTopic(@RequestBody InsertTopicDTO insertTopicDTO, HttpServletRequest req){
        logger.info("Start createTopic - topic owner username : {}", KeycloakUtility.getAccessToken(req).getPreferredUsername());
        Topic savedTopic = topicService.createTopic(insertTopicDTO, req);
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
    public ResponseEntity<EntityModel<Topic>> editTopic(@RequestBody EditTopicDTO topicDTO, HttpServletRequest req){
        logger.info("Start editTopic - topic id: {}", topicDTO.getId());
        Topic editedTopic = topicService.editTopic(topicDTO, req);
        EntityModel<Topic> entityModel = EntityModel.of(editedTopic).add(linkTo(methodOn(TopicRestAPIController.class).editTopic(topicDTO, req)).withSelfRel());
        logger.info("End editTopic");
        return new ResponseEntity<EntityModel<Topic>>(entityModel, HttpStatus.OK);
    }

}
