package com.blackphoenixproductions.forumbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "TOPICS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String message;

    @Column
    private boolean pinned;

    @Column
    private boolean emailUser;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteDate;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date editDate;

    @ManyToOne
    @JoinColumn(name="USER_ID", nullable=false)
    private User user;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
    private Set<Post> posts;

    public Topic(Long id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return id.equals(topic.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
