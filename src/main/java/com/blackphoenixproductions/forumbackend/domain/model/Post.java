package com.blackphoenixproductions.forumbackend.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "POSTS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String message;

    @Column
    private LocalDateTime createDate;

    @Column
    private LocalDateTime deleteDate;

    @Column
    private LocalDateTime editDate;

    @JsonIgnoreProperties({"user", "posts"})
    @ManyToOne
    @JoinColumn(name="TOPIC_ID", nullable=false)
    private Topic topic;

    @JsonIgnoreProperties({"topics", "posts"})
    @ManyToOne
    @JoinColumn(name="USER_ID", nullable=false)
    private User user;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id.equals(post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
