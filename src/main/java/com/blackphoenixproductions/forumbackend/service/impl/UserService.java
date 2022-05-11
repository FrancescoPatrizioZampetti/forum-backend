package com.blackphoenixproductions.forumbackend.service.impl;

import com.blackphoenixproductions.forumbackend.repository.UserRepository;
import com.blackphoenixproductions.forumbackend.service.IUserService;
import com.blackphoenixproductions.forumbackend.entity.User;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public User registerOrRetriveUser(AccessToken accessToken) {
        User findedUser = userRepository.findByEmail(accessToken.getPreferredUsername());
        if(findedUser == null) {
            return userRepository.saveAndFlush(new User(accessToken.getNickName(), accessToken.getPreferredUsername()));
        }
        return findedUser;
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
    @Transactional(readOnly = true)
    public Long getTotalUsers() {
        return userRepository.count();
    }


}
