package com.blackphoenixproductions.forumbackend.adapters.api;


import com.blackphoenixproductions.forumbackend.domain.ports.INotificationService;
import com.blackphoenixproductions.forumbackend.domain.ports.IUserService;
import com.blackphoenixproductions.forumbackend.domain.dto.NotificationDTO;
import com.blackphoenixproductions.forumbackend.domain.entity.User;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        User user = userService.getUserFromEmail(KeycloakUtility.getAccessToken(req).getEmail());
        if(user != null) {
            List<NotificationDTO> userNotification = notificationService.getUserNotification(user);
            if (userNotification != null) {
                userNotificationModel = CollectionModel.of(userNotification, linkTo(methodOn(NotificationRestAPIController.class).getUserNotificationList(req)).withSelfRel());
            }
        }
        logger.info("End getUserNotificationList");
        return new ResponseEntity<CollectionModel<NotificationDTO>>(userNotificationModel, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden.", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "Restituisce lo status delle notifiche di un utente. Permette di capire se un utente ha almeno una notfica da leggere.")
    @GetMapping(value = "getUserNotificationStatus")
    public ResponseEntity<Boolean> getUserNotificationStatus(HttpServletRequest req){
        logger.info("Start getUserNotificationStatus");
        Boolean notificationStatus = null;
        User user = userService.getUserFromEmail(KeycloakUtility.getAccessToken(req).getEmail());
        if(user != null) {
            notificationStatus = notificationService.getUserNotificationStatus(user);
        }
        logger.info("End getUserNotificationStatus");
        return new ResponseEntity<Boolean>(notificationStatus, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden.", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "Imposta lo status delle notifiche di un utente.")
    @PostMapping(value = "setNotificationStatus")
    public ResponseEntity<String> setNotificationStatus(HttpServletRequest req, @Parameter(description = "Determina se mostrare l'avviso di una nuova notifica da leggere.") @RequestParam boolean showNotificationNotice){
        logger.info("Start setNotificationStatus");
        User user = userService.getUserFromEmail(KeycloakUtility.getAccessToken(req).getEmail());
        if(user != null) {
            notificationService.setNotificationStatus(user.getUsername(), showNotificationNotice);
        }
        logger.info("End setNotificationStatus");
        return new ResponseEntity<String>("Le notifiche sono state lette.", HttpStatus.OK);
    }

}
