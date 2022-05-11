package com.blackphoenixproductions.forumbackend.api;


import com.blackphoenixproductions.forumbackend.dto.NotificationDTO;
import com.blackphoenixproductions.forumbackend.entity.User;
import com.blackphoenixproductions.forumbackend.security.KeycloakUtility;
import com.blackphoenixproductions.forumbackend.service.INotificationService;
import com.blackphoenixproductions.forumbackend.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
@Tag(name = "4. Notification", description = "endpoints riguardanti le notifiche.")
public class NotificationRestAPIController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationRestAPIController.class);

    private final INotificationService notificationService;
    private final IUserService userService;


    @Autowired
    public NotificationRestAPIController(INotificationService notificationService, IUserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(summary = "Restituisce tutte le notifiche di un utente.")
    @GetMapping(value = "getUserNotificationList")
    public ResponseEntity<CollectionModel<NotificationDTO>> getUserNotificationList(HttpServletRequest req){
        logger.info("Start getUserNotificationList");
        CollectionModel<NotificationDTO> userNotificationModel = null;
        User user = userService.getUserFromEmail(KeycloakUtility.getAccessToken(req).getPreferredUsername());
        List<NotificationDTO> userNotification = notificationService.getUserNotification(user);
        if(userNotification != null) {
            userNotificationModel = CollectionModel.of(userNotification, linkTo(methodOn(NotificationRestAPIController.class).getUserNotificationList(req)).withSelfRel());
        }
        logger.info("End getUserNotificationList");
        return new ResponseEntity<CollectionModel<NotificationDTO>>(userNotificationModel, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden.", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "Restituisce lo status delle notifiche di un utente. Permette di capire se un utente ha almeno una notfica da leggere.", hidden = true)
    @GetMapping(value = "getUserNotificationStatus")
    public ResponseEntity<Boolean> getUserNotificationStatus(HttpServletRequest req){
        logger.info("Start getUserNotificationStatus");
        User user = userService.getUserFromEmail(KeycloakUtility.getAccessToken(req).getPreferredUsername());
        Boolean notificationStatus = notificationService.getUserNotificationStatus(user);
        logger.info("End getUserNotificationStatus");
        return new ResponseEntity<Boolean>(notificationStatus, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden.", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "Imposta lo status delle notifiche di un utente a 'tutte lette'.", hidden = true)
    @GetMapping(value = "setReadedNotificationStatus")
    public ResponseEntity<String> setReadedNotificationStatus(HttpServletRequest req){
        logger.info("Start setReadedNotificationStatus");
        User user = userService.getUserFromEmail(KeycloakUtility.getAccessToken(req).getPreferredUsername());
        notificationService.setReadedNotificationStatus(user);
        logger.info("End setReadedNotificationStatus");
        return new ResponseEntity<String>("Le notifiche sono state lette.", HttpStatus.OK);
    }

}
