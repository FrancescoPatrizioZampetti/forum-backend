package com.blackphoenixproductions.forumbackend.service;

import com.blackphoenixproductions.forumbackend.entity.User;
import dto.SimpleUserDTO;
import dto.UserDTO;

import javax.servlet.http.HttpServletRequest;

public interface IUserService {

    String login(String username, String password);

    String signin(UserDTO userDTO);

    SimpleUserDTO getUserFromToken(HttpServletRequest req);

    SimpleUserDTO getUserFromUsername(String username);

    UserDTO getUserFromEmail (String email);

    String getJwtToken(String username, String role);

    Long getTotalUsers();

    String refresh(String username);

    String getResetToken (String username, String email);

    String getNewAccessTokenFromRefreshToken(HttpServletRequest req);

    User finishResetCredentials(String password, User user);

    boolean isValidResetToken (String token, User user);

    User getUserFromEmailWithRoleUser (String email);

    User getUserFromUsernameWithRoleUser (String username);

}
