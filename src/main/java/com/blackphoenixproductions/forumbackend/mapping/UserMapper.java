package com.blackphoenixproductions.forumbackend.mapping;

import com.blackphoenixproductions.forumbackend.entity.User;
import dto.SimpleUserDTO;
import dto.UserDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    UserDTO userToUserDTO(User user);
    User userDTOtoUser(UserDTO userDTO);
    SimpleUserDTO userToSimpleUserDTO(User user);

}
