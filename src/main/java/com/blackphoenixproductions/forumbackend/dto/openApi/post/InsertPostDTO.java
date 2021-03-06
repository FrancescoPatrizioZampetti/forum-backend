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

    public InsertPostDTO(String message, Long topicId) {
        this.message = message;
        this.topicId = topicId;
    }
}
