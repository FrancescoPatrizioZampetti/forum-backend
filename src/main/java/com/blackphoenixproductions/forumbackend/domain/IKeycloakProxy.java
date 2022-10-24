package com.blackphoenixproductions.forumbackend.domain;

import org.keycloak.representations.AccessToken;

public interface IKeycloakProxy {
    void changeUserUsername(AccessToken accessToken, String newUsername);
}
