package com.blackphoenixproductions.forumbackend.service.impl;

import com.blackphoenixproductions.forumbackend.mapping.UserMapper;
import com.blackphoenixproductions.forumbackend.repository.UserRepository;
import com.blackphoenixproductions.forumbackend.service.IUserService;
import dto.SimpleUserDTO;
import dto.UserDTO;
import com.blackphoenixproductions.forumbackend.entity.User;
import com.blackphoenixproductions.forumbackend.security.JwtTokenProvider;
import dto.openApi.exception.CustomException;
import enums.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService implements IUserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserMapper userMapper, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional
    public String login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            User findedUser = userRepository.findByUsername(username);
            return jwtTokenProvider.createToken(username, findedUser.getRole());
        } catch (AuthenticationException e) {
            throw new CustomException("Username/password non valido", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Override
    @Transactional
    public String signin(UserDTO userDTO) {
        User findedUser = userRepository.findByUsernameOrEmail(userDTO.getUsername(), userDTO.getEmail());
        if(findedUser == null) {
            User user = userMapper.userDTOtoUser(userDTO);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return jwtTokenProvider.createToken(user.getUsername(), user.getRole());
        } else {
            throw new CustomException("Username e/o Email gia' utilizzati", HttpStatus.CONFLICT);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public SimpleUserDTO getUserFromToken(HttpServletRequest req) {
        // ricavo username dal token
        return userRepository.findUserByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
    }

    @Override
    @Transactional(readOnly = true)
    public SimpleUserDTO getUserFromUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserFromEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public String getJwtToken(String username, String role) {
        return jwtTokenProvider.createToken(username, role);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getTotalUsers() {
        return userRepository.count();
    }

    @Override
    public String refresh(String username) {
        return jwtTokenProvider.createRefreshToken(username);
    }

    @Override
    public String getResetToken(String username, String password) {
        return jwtTokenProvider.createResetToken(username, password );
    }

    @Override
    @Transactional(readOnly = true)
    public String getNewAccessTokenFromRefreshToken(HttpServletRequest req) {
        String newAccessToken = null;
        SimpleUserDTO simpleUserDTO = getUserFromToken(req);
        if (simpleUserDTO != null)
            newAccessToken = jwtTokenProvider.createToken(simpleUserDTO.getUsername(), simpleUserDTO.getRole());
        return newAccessToken;
    }

    @Override
    @Transactional
    public User finishResetCredentials(String password, User user) {
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Override
    public boolean isValidResetToken(String token, User user) {
        return jwtTokenProvider.validateResetToken(token, user.getPassword());
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserFromEmailWithRoleUser(String email) {
        return userRepository.findByEmailAndRole(email, Roles.ROLE_USER.getValue());
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserFromUsernameWithRoleUser(String username) {
        return userRepository.findByEmailAndRole(username, Roles.ROLE_USER.getValue());
    }


}
