package com.blackphoenixproductions.forumbackend.api;


import com.blackphoenixproductions.forumbackend.entity.User;
import com.blackphoenixproductions.forumbackend.repository.UserRepository;
import com.blackphoenixproductions.forumbackend.security.KeycloakUtility;
import com.blackphoenixproductions.forumbackend.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
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
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserRestAPIController.class);

    @Autowired
    public UserRestAPIController(IUserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }


    @Operation(summary = "Restituisce il numero totale degli utenti.")
    @GetMapping (value = "/getTotalUsers")
    public ResponseEntity<Long> getTotalUsers (HttpServletRequest req){
        Long totalUsers = userService.getTotalUsers();
        return new ResponseEntity<Long>(totalUsers, HttpStatus.OK);
    }

    @Operation(summary = "Restituisce l'utente loggato.")
    @GetMapping (value = "/findUser")
    public ResponseEntity<EntityModel<User>> findUser (HttpServletRequest req){
        logger.info("Start findUser");
        User findedUser = userService.getUserFromEmail(KeycloakUtility.getAccessToken(req).getEmail());
        logger.info("End findUser");
        return new ResponseEntity<EntityModel<User>>(EntityModel.of(findedUser).add(linkTo(methodOn(UserRestAPIController.class).findUser(req)).withSelfRel()), HttpStatus.OK);
    }


}