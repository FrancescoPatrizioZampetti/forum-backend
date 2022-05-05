package com.blackphoenixproductions.forumbackend.entity;

import dto.SimpleUserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SqlResultSetMapping(name="SimpleUserDTOResult", classes = {
        @ConstructorResult(targetClass = SimpleUserDTO.class,
                columns = {@ColumnResult(name="id", type=Long.class), @ColumnResult(name="username", type=String.class),
                        @ColumnResult(name="email", type=String.class), @ColumnResult(name="role", type=String.class)})
})
@NamedNativeQuery(name="User.findAllUsers",
        resultSetMapping="SimpleUserDTOResult",
        query="SELECT u.id, u.username, u.email, u.role from users u")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column
    private String password;

    @Column
    private String role;

    @OneToMany(mappedBy = "user")
    private Set<Post> posts;

    @OneToMany(mappedBy = "user")
    private Set<Topic> topics;



    /**
     * NB: Non è possibile stabilire l'uguaglianza di due instanze transient.
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
