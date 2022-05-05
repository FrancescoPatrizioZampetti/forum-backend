package com.blackphoenixproductions.forumbackend.repository;

import com.blackphoenixproductions.forumbackend.entity.User;
import com.blackphoenixproductions.forumbackend.repository.projection.IUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    User findByUsername(String username);
    User findByUsernameOrEmail(String username, String email);
    User findByEmailAndRole(String email, String role);
    User findByUsernameAndRole(String email, String role);
    @Query(value = "SELECT u.username, u.role from users u", nativeQuery=true)
    List<IUser> findUser();
    @Query(value = "SELECT u.role from users u where u.username = ?1", nativeQuery=true)
    String findUserRole(String username);


}
