package com.blackphoenixproductions.forumbackend.dto.openApi.topic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Il topic da modificare.")
public class EditTopicDTO {
    @Schema(description = "L'id del topic.", required = true)
    private Long id;
    @Schema(description = "Il messaggio del topic.", required = true)
    private String message;
}
