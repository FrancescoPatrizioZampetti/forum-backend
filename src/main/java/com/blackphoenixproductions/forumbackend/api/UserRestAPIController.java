/*
package com.blackphoenixproductions.forumbackend.api;


import com.blackphoenixproductions.forumbackend.email.EmailSender;
import com.blackphoenixproductions.forumbackend.entity.User;
import com.blackphoenixproductions.forumbackend.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
@Tag(name = "1. User", description = "endpoints riguardanti gli utenti.")
public class UserRestAPIController {

    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;

    private static final Logger logger = LoggerFactory.getLogger(UserRestAPIController.class);

    @Autowired
    public UserRestAPIController(IUserService userService, PasswordEncoder passwordEncoder, EmailSender emailSender) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.emailSender = emailSender;
    }





    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "409", description = "Conflict: Username e/o Email gia' utilizzati.", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "Registrazione.")
    @PostMapping (value = "/signin")
    public ResponseEntity<EntityModel<TokenContainerDTO>> signin (@RequestBody UserDTO userDTO) {
        logger.info("Start signin - username : {}", userDTO.getUsername());
        TokenContainerDTO tokenContainerDTO = null;
        userDTO.setRole(Roles.ROLE_USER.getValue());
        String jwtToken = userService.signin(userDTO);
        String refreshJwtToken = userService.refresh(userDTO.getUsername());
        tokenContainerDTO = new TokenContainerDTO(jwtToken, refreshJwtToken);
        emailSender.sendSigninEmail(userDTO);
        EntityModel<TokenContainerDTO> tokenContainerModel = EntityModel.of(tokenContainerDTO, linkTo(methodOn(UserRestAPIController.class).signin(userDTO)).withSelfRel());
        logger.info("End signin");
        return new ResponseEntity<EntityModel<TokenContainerDTO>>(tokenContainerModel, HttpStatus.OK);
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Forbidden.", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "Restuisce l'utente a cui appartiene il token.")
    @GetMapping (value = "/getUserFromToken")
    public ResponseEntity<EntityModel<SimpleUserDTO>> getUserFromToken (HttpServletRequest req){
        SimpleUserDTO simpleUserDTO = userService.getUserFromToken(req);
        EntityModel<SimpleUserDTO> simpleUserDTOModel = EntityModel.of(simpleUserDTO, linkTo(methodOn(UserRestAPIController.class).getUserFromToken(req)).withSelfRel());
        return new ResponseEntity<EntityModel<SimpleUserDTO>>(simpleUserDTOModel, HttpStatus.OK);
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
    public ResponseEntity finishResetCredentials(@RequestParam String username, @RequestParam String password, @RequestParam String token) {
        logger.info("Start finishResetCredentials - username: {}", username);
        User userDB = userService.getUserFromUsernameWithRoleUser(username);
        if(userDB != null) {
            userService.isValidResetToken(token, userDB);
            userService.finishResetCredentials(password, userDB);
        } else{
            logger.error("Utente con ruolo USER non trovato durante il completamento della procedura di reset.");
        }
        logger.info("End finishResetCredentials");
        return ResponseEntity.ok("Procedura di reset password completata con successo.");
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request.", content = @Content(schema = @Schema(hidden = true)) ),
    })
    @Operation(summary = "Manda una email di reset credenziali creando un reset token.")
    @PostMapping("/initResetCredentials")
    public ResponseEntity initResetCredentials(@RequestParam String email) {
        logger.info("Start initResetCredentials");
        User userDB = userService.getUserFromEmailWithRoleUser(email);
        // non restituisco exception per motivi di sicurezza
        if (userDB != null) {
            String resetToken = userService.getResetToken(userDB.getUsername(), userDB.getPassword());
            emailSender.sendResetCredentialsEmail(userDB, resetToken);
        } else{
            logger.error("Utente con ruolo USER non trovato durante la procedura di reset.");
        }
        logger.info("End initResetCredentials");
        return ResponseEntity.ok("Procedura di reset password iniziata con successo.");
    }


    private String signInSocialUser(@RequestParam String email, UserDTO newUser) {
        logger.info("Start signInSocialUser");
        // genero username e password randomicamente
        newUser.setUsername(RandomStringUtils.random(8, true, true));
        newUser.setPassword(passwordEncoder.encode(RandomStringUtils.random(8, true, true)));
        newUser.setEmail(email);
        // registro l'utente
        logger.info("End signInSocialUser");
        return userService.signin(newUser);
    }


}

 */