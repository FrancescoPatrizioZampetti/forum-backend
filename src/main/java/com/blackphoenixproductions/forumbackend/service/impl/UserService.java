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

    /**
     * Recupera l'utente registrato.
     * Nota bene: l'utente viene persistito nell'applicativo durante la registrazione con Keycloak,
     * tuttavia se non lo trova lo salva.
     * @param accessToken
     * @return
     */
    @Override
    @Transactional
    public User retriveUser(AccessToken accessToken) {
        User findedUser = userRepository.findByEmail(accessToken.getEmail());
        if(findedUser == null) {
            return userRepository.saveAndFlush(new User(accessToken.getPreferredUsername(), accessToken.getEmail()));
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
