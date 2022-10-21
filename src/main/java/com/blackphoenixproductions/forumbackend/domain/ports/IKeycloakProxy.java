package com.blackphoenixproductions.forumbackend.domain.ports;

import com.blackphoenixproductions.forumbackend.domain.model.User;
import org.keycloak.representations.AccessToken;

public interface IKeycloakProxy {
    User changeUserUsername(AccessToken accessToken, String newUsername);
}
