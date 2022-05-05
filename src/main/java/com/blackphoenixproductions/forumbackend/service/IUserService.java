package com.blackphoenixproductions.forumbackend.service;

import com.blackphoenixproductions.forumbackend.entity.User;


import javax.servlet.http.HttpServletRequest;

public interface IUserService {

    User signin(User user);

    User getUserFromToken(HttpServletRequest req);

    User getUserFromUsername(String username);

    User getUserFromEmail (String email);

    Long getTotalUsers();

    User finishResetCredentials(String password, User user);

}
