package com.blackphoenixproductions.forumbackend.service.impl;

import com.blackphoenixproductions.forumbackend.dto.openApi.exception.CustomException;
import com.blackphoenixproductions.forumbackend.repository.UserRepository;
import com.blackphoenixproductions.forumbackend.service.IUserService;
import com.blackphoenixproductions.forumbackend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * TODO SALVARE L'UTENTE SOLO DURANTE INSERIMENTO TOPIC/POST (SE NON ESISTE)
     * @param user
     * @return
     */
    @Override
    @Transactional
    public User signin(User user) {
        User findedUser = userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail());
        if(findedUser == null) {
            return userRepository.saveAndFlush(user);
        } else {
            throw new CustomException("Username e/o Email gia' utilizzati", HttpStatus.CONFLICT);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserFromToken(HttpServletRequest req) {
        // todo
        return null;
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
        // todo tramite keycloak
        return null;
    }

}
