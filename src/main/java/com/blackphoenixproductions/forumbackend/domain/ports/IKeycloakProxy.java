package com.blackphoenixproductions.forumbackend.domain.ports;

import org.keycloak.representations.AccessToken;

public interface IKeycloakProxy {
    void changeUserUsername(AccessToken accessToken, String newUsername);
}
