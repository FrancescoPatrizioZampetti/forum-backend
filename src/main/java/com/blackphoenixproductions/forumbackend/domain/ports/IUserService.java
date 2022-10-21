package com.blackphoenixproductions.forumbackend.domain.ports;

import com.blackphoenixproductions.forumbackend.domain.model.User;
import org.keycloak.representations.AccessToken;

import java.util.Set;


public interface IUserService {

    User getUserFromUsername(String username);

    User getUserFromEmail (String email);

    User changeUserUsername (AccessToken accessToken, String username);

    Long getTotalUsers();

    User retriveUser(String username);

}
