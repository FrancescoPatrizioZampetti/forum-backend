package com.blackphoenixproductions.forumbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Immutable
@Entity
@Table(name = "V_TOPICS")
public class VTopic {

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

    @Column
    private String authorUsername;

    @Column
    private String authorEmail;

}
