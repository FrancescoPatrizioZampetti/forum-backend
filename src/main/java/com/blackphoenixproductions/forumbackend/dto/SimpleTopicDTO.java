package com.blackphoenixproductions.forumbackend.dto;

import dto.SimpleUserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleTopicDTO {

    private Long id;
    private String title;
    private String message;
    private boolean pinned;
    private boolean emailUser;
    private Date createDate;
    private SimpleUserDTO user;
    private int postsNumber;
    private String timeDifferenceFromNow;

    public SimpleTopicDTO(Long id) {
        this.id = id;
    }
}
