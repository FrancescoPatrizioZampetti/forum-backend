package com.blackphoenixproductions.forumbackend.domain.service;

import com.blackphoenixproductions.forumbackend.domain.ports.IKeycloakProxy;
import com.blackphoenixproductions.forumbackend.domain.ports.IUserService;
import com.blackphoenixproductions.forumbackend.domain.model.User;
import com.blackphoenixproductions.forumbackend.domain.ports.repository.UserRepository;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService implements IUserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final IKeycloakProxy keycloakProxy;

    @Autowired
    public UserService(UserRepository userRepository,
                       IKeycloakProxy keycloakProxy)  {
        this.userRepository = userRepository;
        this.keycloakProxy = keycloakProxy;
    }

    @Override
    public User retriveUser(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserFromUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserFromEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public User changeUserUsername(AccessToken accessToken, String username) {
        User user = retriveUser(username);
        user.setUsername(username);
        user = userRepository.saveAndFlush(user);
        keycloakProxy.changeUserUsername(accessToken, username);
        return user;
    }


    @Override
    @Transactional(readOnly = true)
    public Long getTotalUsers() {
        return userRepository.count();
    }


}
