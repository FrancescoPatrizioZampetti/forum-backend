package com.blackphoenixproductions.forumbackend.dto.openApi.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Il post da creare.")
public class InsertPostDTO {
    @Schema(description = "Il messaggio del post.", required = true)
    private String message;
    @Schema(description = "L'id del del topic a cui appartiene il post.", required = true)
    private Long topicId;
    @Schema(description = "L'username dell'autore del post.", required = true)
    private String username;

    public InsertPostDTO(String message, Long topicId, String username) {
        this.message = message;
        this.topicId = topicId;
        this.username = username;
    }
}
