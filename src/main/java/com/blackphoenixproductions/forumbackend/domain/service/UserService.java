package com.blackphoenixproductions.forumbackend.domain.service;

import com.blackphoenixproductions.forumbackend.domain.ports.IUserService;
import com.blackphoenixproductions.forumbackend.adapters.dto.CustomException;
import com.blackphoenixproductions.forumbackend.domain.model.User;
import com.blackphoenixproductions.forumbackend.domain.enums.Roles;
import com.blackphoenixproductions.forumbackend.domain.ports.repository.UserRepository;
import com.blackphoenixproductions.forumbackend.config.security.KeycloakUtility;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Service
public class UserService implements IUserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private String KEYCLOAK_SERVER_URL;
    private String KEYCLOAK_REALM;
    private String KEYCLOAK_RESOURCE;
    private String SERVICE_USER_USERNAME;
    private String SERVICE_USER_PASSWORD;

    @Autowired
    public UserService(UserRepository userRepository,
                       @Value("${keycloak.auth-server-url}") String KEYCLOAK_SERVER_URL,
                       @Value("${keycloak.realm}") String KEYCLOAK_REALM,
                       @Value("${keycloak.resource}") String KEYCLOAK_RESOURCE,
                       @Value("${service.user.username}") String SERVICE_USER_USERNAME,
                       @Value("${service.user.password}") String SERVICE_USER_PASSWORD)  {
        this.userRepository = userRepository;
        this.KEYCLOAK_SERVER_URL = KEYCLOAK_SERVER_URL;
        this.KEYCLOAK_REALM = KEYCLOAK_REALM;
        this.KEYCLOAK_RESOURCE = KEYCLOAK_RESOURCE;
        this.SERVICE_USER_USERNAME = SERVICE_USER_USERNAME;
        this.SERVICE_USER_PASSWORD = SERVICE_USER_PASSWORD;
    }

    /**
     * Recupera l'utente registrato.
     * Nota bene: l'utente viene persistito nell'applicativo durante la registrazione con Keycloak tramite un plugin custom,
     * tuttavia se non dovesse trovarlo (anomalia o demo) lo salva ora.
     * @param accessToken
     * @return
     */
    @Override
    @Transactional
    public User retriveUser(AccessToken accessToken) {
        User findedUser = userRepository.findByEmail(accessToken.getEmail());
        if(findedUser == null) {
            logger.warn("Utente non trovato nell'applicativo, procedo a salvarlo...");
            findedUser = userRepository.saveAndFlush(new User(accessToken.getPreferredUsername(), accessToken.getEmail(), getApplicationRole(KeycloakUtility.getRoles(accessToken))));
        }
        return findedUser;
    }

    private String getApplicationRole(Set<String> roles) {
        if(roles.contains(Roles.ROLE_HELPDESK.getValue())){
            return Roles.ROLE_HELPDESK.getValue();
        } else if(roles.contains(Roles.ROLE_STAFF.getValue())){
            return Roles.ROLE_STAFF.getValue();
        }
        return Roles.ROLE_USER.getValue();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserFromUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserFromEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public User changeUserUsername(AccessToken accessToken, String username) {
        User user = retriveUser(accessToken);
        user.setUsername(username);
        user = userRepository.saveAndFlush(user);
        try {
            Keycloak kc = KeycloakBuilder.builder()
                    .serverUrl(KEYCLOAK_SERVER_URL)
                    .realm(KEYCLOAK_REALM)
                    .username(SERVICE_USER_USERNAME)
                    .password(SERVICE_USER_PASSWORD)
                    .clientId(KEYCLOAK_RESOURCE)
                    .build();

            UserRepresentation keycloakUser = kc.realm(KEYCLOAK_REALM).users().get(accessToken.getSubject()).toRepresentation();
            keycloakUser.setUsername(username);
            kc.realm(KEYCLOAK_REALM).users().get(accessToken.getSubject()).update(keycloakUser);

        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return user;
    }


    @Override
    @Transactional(readOnly = true)
    public Long getTotalUsers() {
        return userRepository.count();
    }


}
