package com.blackphoenixproductions.forumbackend.service.impl;

import com.blackphoenixproductions.forumbackend.dto.openApi.exception.CustomException;
import com.blackphoenixproductions.forumbackend.repository.UserRepository;
import com.blackphoenixproductions.forumbackend.service.IUserService;
import com.blackphoenixproductions.forumbackend.entity.User;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


@Service
public class UserService implements IUserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public UserService(UserRepository userRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    /**
     * Recupera l'utente registrato.
     * Nota bene: l'utente viene persistito nell'applicativo durante la registrazione con Keycloak tramite un plugin custom,
     * tuttavia se non dovesse trovarlo (anomalia) lo salva ora.
     * @param accessToken
     * @return
     */
    @Override
    @Transactional
    public User retriveUser(AccessToken accessToken) {
        User findedUser = userRepository.findByEmail(accessToken.getEmail());
        if(findedUser == null) {
            logger.warn("Utente non trovato nell'applicativo, procedo a salvarlo...");
            return userRepository.saveAndFlush(new User(accessToken.getPreferredUsername(), accessToken.getEmail(), accessToken.getId()));
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
    @Transactional
    public User changeUserUsername(String email, String username) {
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new CustomException("Utente non trovato.", HttpStatus.NOT_FOUND);
        }
        user.setUsername(username);
        user = userRepository.saveAndFlush(user);


        // chiamo keycloak, l'id è quello di keycloak
        // GET /{realm}/users/{id}
        // PUT /{realm}/users/{id}
        // nel body UserRepresentation

        return user;
    }


    @Override
    @Transactional(readOnly = true)
    public Long getTotalUsers() {
        return userRepository.count();
    }


}
