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
     * TODO SALVARE L'UTENTE SOLO DURANTE INSERIMENTO TOPIC/POST (SE NON ESISTE)
     * @param
     * @return
     */
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


    @Override
    @Transactional
    public User finishResetCredentials(String password, User user) {
        // todo tramite keycloak, vanno aggiornate le credenziali tramite api
        return null;
    }

}
