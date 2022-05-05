package com.blackphoenixproductions.forumbackend.dto;

import dto.PostDTO;
import dto.SimpleUserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopicDTO {

    private Long id;
    private String title;
    private String message;
    private boolean pinned;
    private boolean emailUser;
    private SimpleUserDTO user;
    private Set<PostDTO> posts;
    private String formattedDate;
    private String formattedEditDate;

}
