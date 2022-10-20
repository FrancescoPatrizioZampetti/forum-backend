package com.blackphoenixproductions.forumbackend.domain.dto.openApi.topic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Schema(description = "Il topic da modificare.")
public class EditTopicDTO {
    @NotNull(message = "L'id del topic non puo' essere null")
    @Schema(description = "L'id del topic.", required = true)
    private Long id;
    @NotEmpty(message = "Il messaggio del topic non puo' essere null/vuoto")
    @Size(max = 20000, message = "Il messaggio del topic ha superato il massimo di 20000 caratteri")
    @Schema(description = "Il messaggio del topic.", required = true)
    private String message;
}
