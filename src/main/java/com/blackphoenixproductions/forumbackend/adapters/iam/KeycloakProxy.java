package com.blackphoenixproductions.forumbackend.adapters.iam;

import com.blackphoenixproductions.forumbackend.adapters.api.dto.CustomException;
import com.blackphoenixproductions.forumbackend.domain.ports.IKeycloakProxy;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class KeycloakProxy implements IKeycloakProxy {
    private String KEYCLOAK_SERVER_URL;
    private String KEYCLOAK_REALM;
    private String KEYCLOAK_RESOURCE;
    private String SERVICE_USER_USERNAME;
    private String SERVICE_USER_PASSWORD;

    @Autowired
    public KeycloakProxy( @Value("${keycloak.auth-server-url}") String KEYCLOAK_SERVER_URL,
                       @Value("${keycloak.realm}") String KEYCLOAK_REALM,
                       @Value("${keycloak.resource}") String KEYCLOAK_RESOURCE,
                       @Value("${service.user.username}") String SERVICE_USER_USERNAME,
                       @Value("${service.user.password}") String SERVICE_USER_PASSWORD)  {
        this.KEYCLOAK_SERVER_URL = KEYCLOAK_SERVER_URL;
        this.KEYCLOAK_REALM = KEYCLOAK_REALM;
        this.KEYCLOAK_RESOURCE = KEYCLOAK_RESOURCE;
        this.SERVICE_USER_USERNAME = SERVICE_USER_USERNAME;
        this.SERVICE_USER_PASSWORD = SERVICE_USER_PASSWORD;
    }

    @Override
    public void changeUserUsername(AccessToken accessToken, String newUsername) {
        try {
            Keycloak kc = KeycloakBuilder.builder()
                    .serverUrl(KEYCLOAK_SERVER_URL)
                    .realm(KEYCLOAK_REALM)
                    .username(SERVICE_USER_USERNAME)
                    .password(SERVICE_USER_PASSWORD)
                    .clientId(KEYCLOAK_RESOURCE)
                    .build();

            UserRepresentation keycloakUser = kc.realm(KEYCLOAK_REALM).users().get(accessToken.getSubject()).toRepresentation();
            keycloakUser.setUsername(newUsername);
            kc.realm(KEYCLOAK_REALM).users().get(accessToken.getSubject()).update(keycloakUser);
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
