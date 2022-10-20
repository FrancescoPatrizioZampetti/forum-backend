package com.blackphoenixproductions.forumbackend.adapters.api;


import com.blackphoenixproductions.forumbackend.domain.entity.User;
import com.blackphoenixproductions.forumbackend.config.security.KeycloakUtility;
import com.blackphoenixproductions.forumbackend.domain.ports.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/api")
@Tag(name = "1. User", description = "endpoints riguardanti gli utenti.")
public class UserRestAPIController {

    private final IUserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserRestAPIController.class);

    @Autowired
    public UserRestAPIController(IUserService userService) {
        this.userService = userService;;
    }


    @Operation(summary = "Restituisce il numero totale degli utenti.")
    @GetMapping (value = "/getTotalUsers")
    public ResponseEntity<Long> getTotalUsers (HttpServletRequest req){
        Long totalUsers = userService.getTotalUsers();
        return new ResponseEntity<Long>(totalUsers, HttpStatus.OK);
    }

    @Operation(summary = "Cerca l'utente loggato nell'applicativo.")
    @GetMapping (value = "/retriveUser")
    public ResponseEntity<EntityModel<User>> retriveUser (HttpServletRequest req){
        logger.info("Start retriveUser");
        User findedUser = userService.retriveUser(KeycloakUtility.getAccessToken(req));
        logger.info("End retriveUser");
        return new ResponseEntity<EntityModel<User>>(EntityModel.of(findedUser).add(linkTo(methodOn(UserRestAPIController.class).retriveUser(req)).withSelfRel()), HttpStatus.OK);
    }

    @Operation(summary = "Cambia l'username dell'utente loggato.")
    @PostMapping (value = "/changeUserUsername")
    public ResponseEntity<EntityModel<User>> changeUserUsername (HttpServletRequest req, @Parameter(description = "Il nuovo username") @RequestParam String newUsername){
        logger.info("Start changeUserUsername");
        User user = userService.changeUserUsername(KeycloakUtility.getAccessToken(req), newUsername);
        logger.info("End changeUserUsername");
        return new ResponseEntity<EntityModel<User>>(EntityModel.of(user).add(linkTo(methodOn(UserRestAPIController.class).changeUserUsername(req, newUsername)).withSelfRel()), HttpStatus.OK);
    }


}