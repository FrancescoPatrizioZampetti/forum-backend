package com.blackphoenixproductions.forumbackend.api;


import com.blackphoenixproductions.forumbackend.email.EmailSender;
import com.blackphoenixproductions.forumbackend.entity.User;
import com.blackphoenixproductions.forumbackend.security.KeycloakUtility;
import com.blackphoenixproductions.forumbackend.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.RandomStringUtils;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
@Tag(name = "1. User", description = "endpoints riguardanti gli utenti.")
public class UserRestAPIController {

    private final IUserService userService;
    private final EmailSender emailSender;

    private static final Logger logger = LoggerFactory.getLogger(UserRestAPIController.class);

    @Autowired
    public UserRestAPIController(IUserService userService, EmailSender emailSender) {
        this.userService = userService;
        this.emailSender = emailSender;
    }



    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "409", description = "Conflict: Username e/o Email gia' utilizzati.", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "Registrazione.")
    @PostMapping (value = "/signin")
    public ResponseEntity<EntityModel<User>> signin (HttpServletRequest req, @RequestBody User user) {
        // todo capire come interagire con keycloak, prendere email dalla request e poi chiamare kc?
        // todo va chiamato anche in caso di registrazione social, in caso generare randomicamente username?
        logger.info("Start signin - username : {}", user.getUsername());
        User savedUser = userService.signin(user);
        emailSender.sendSigninEmail(user);
        EntityModel<User> userEntityModel = EntityModel.of(user, linkTo(methodOn(UserRestAPIController.class).signin(req, user)).withSelfRel());
        logger.info("End signin");
        return new ResponseEntity<EntityModel<User>>(userEntityModel, HttpStatus.OK);
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Forbidden.", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "Restuisce l'utente a cui appartiene il token.")
    @GetMapping (value = "/getUserFromToken")
    public ResponseEntity<EntityModel<User>> getUserFromToken (HttpServletRequest req){
        // todo prendere mail dalla request
        Set<String> roles = KeycloakUtility.getRoles(req);
        AccessToken token = KeycloakUtility.getAccessToken(req);

        User user = userService.getUserFromToken(req);
        EntityModel<User> userEntityModel = EntityModel.of(user, linkTo(methodOn(UserRestAPIController.class).getUserFromToken(req)).withSelfRel());
        return new ResponseEntity<EntityModel<User>>(userEntityModel, HttpStatus.OK);
    }


    @Operation(summary = "Restituisce il numero totale degli utenti.")
    @GetMapping (value = "/getTotalUsers")
    public ResponseEntity<Long> getTotalUsers (HttpServletRequest req){
        Long totalUsers = userService.getTotalUsers();
        return new ResponseEntity<Long>(totalUsers, HttpStatus.OK);
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error: JWT token scaduto oppure non valido.", content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(summary = "Endpoint che permette il reset delle credenziali.")
    @PostMapping("/finishResetCredentials")
    public ResponseEntity finishResetCredentials(HttpServletRequest req) {
        logger.info("Start finishResetCredentials");
        // todo vedere come implementarlo chiamando kc e verificando un token generato precedentemente
        //userService.finishResetCredentials();
        logger.info("End finishResetCredentials");
        return ResponseEntity.ok("Procedura di reset password completata con successo.");
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = @Content(schema = @Schema(hidden = true)) ),
    })
    @Operation(summary = "Manda una email di reset credenziali creando un reset token.")
    @PostMapping("/initResetCredentials")
    public ResponseEntity initResetCredentials(HttpServletRequest req) {
        logger.info("Start initResetCredentials");
        // todo recuperare email dalla req e poi capire come implementarlo con kc
        // emailSender.sendResetCredentialsEmail(userDB, resetToken);
        logger.info("End initResetCredentials");
        return ResponseEntity.ok("Procedura di reset password iniziata con successo.");
    }

}