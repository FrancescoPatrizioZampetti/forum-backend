package com.blackphoenixproductions.forumbackend.dto.openApi.topic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Schema(description = "Il topic da modificare.")
public class EditTopicDTO {
    @NotNull(message = "L'id del topic non puo' essere null")
    @Schema(description = "L'id del topic.", required = true)
    private Long id;
    @NotBlank(message = "Il messaggio del topic non puo' essere vuoto/null")
    @Schema(description = "Il messaggio del topic.", required = true)
    private String message;
}
