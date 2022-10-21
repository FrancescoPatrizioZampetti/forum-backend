package com.blackphoenixproductions.forumbackend.domain.ports;

import com.blackphoenixproductions.forumbackend.domain.model.User;
import org.keycloak.representations.AccessToken;

import java.util.Set;


public interface IUserService {

    User getUserFromUsername(String username);

    User getUserFromEmail (String email);

    User changeUserUsername (String email, String newUsername);

    Long getTotalUsers();

    User retriveUser(String username);

}
