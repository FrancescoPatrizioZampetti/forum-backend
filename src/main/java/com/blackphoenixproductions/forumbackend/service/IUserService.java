package com.blackphoenixproductions.forumbackend.service;

import com.blackphoenixproductions.forumbackend.entity.User;
import org.keycloak.representations.AccessToken;



public interface IUserService {

    User registerOrRetriveUser(AccessToken accessToken);

    User getUserFromUsername(String username);

    User getUserFromEmail (String email);

    Long getTotalUsers();

}
