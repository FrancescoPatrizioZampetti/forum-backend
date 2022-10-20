package com.blackphoenixproductions.forumbackend.domain.ports.repository;

import com.blackphoenixproductions.forumbackend.domain.model.User;
import com.blackphoenixproductions.forumbackend.domain.ports.repository.projection.IUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    User findByUsername(String username);
    User findByUsernameOrEmail(String username, String email);
    @Query(value = "SELECT u.username, u.email from users u", nativeQuery=true)
    List<IUser> findUser();
    @Query(value = "SELECT u.email from users u where u.username = ?1", nativeQuery=true)
    String findUserEmail(String username);

}
