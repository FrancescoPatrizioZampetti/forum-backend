package com.blackphoenixproductions.forumbackend.domain.ports;

import com.blackphoenixproductions.forumbackend.domain.model.User;
import org.keycloak.representations.AccessToken;



public interface IUserService {

    User retriveUser(AccessToken accessToken);

    User getUserFromUsername(String username);

    User getUserFromEmail (String email);

    User changeUserUsername (AccessToken accessToken, String username);

    Long getTotalUsers();

}
