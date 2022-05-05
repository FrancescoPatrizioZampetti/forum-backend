package com.blackphoenixproductions.forumbackend.dto;

import dto.SimpleTopicDTO;
import dto.SimpleUserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    private Long id;
    private String message;
    private SimpleTopicDTO topic;
    private SimpleUserDTO user;
    private String formattedCreateDate;
    private String formattedEditDate;

}
