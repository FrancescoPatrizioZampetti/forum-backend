package com.blackphoenixproductions.forumbackend.domain.ports;

import com.blackphoenixproductions.forumbackend.domain.entity.User;


public interface IUserService {

    User getUserFromUsername(String username);

    User getUserFromEmail (String email);

    User changeUserUsername (String email, String newUsername);

    Long getTotalUsers();

    User retriveUser(String username);

}
