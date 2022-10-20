package com.blackphoenixproductions.forumbackend.domain.dto.openApi.topic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@Schema(description = "Il topic da creare.")
public class InsertTopicDTO {
    @NotEmpty(message = "Il titolo del topic non puo' essere null/vuoto")
    @Size(min = 5, max = 50, message = "Il titolo del topic puo' avere un minimo di 5 caratteri e un massimo di 50")
    @Schema(description = "Il titolo del topic", required = true)
    private String title;
    @NotEmpty(message = "Il messaggio del topic non puo' essere null/vuoto")
    @Size(max = 20000, message = "Il messaggio del topic ha superato il massimo di 20000 caratteri")
    @Schema(description = "Il messaggio del topic.", required = true)
    private String message;
    @Schema(description = "Indica se dovrà essere messo in evidenza.", required = true)
    private boolean pinned;
    @Schema(description = "Indica se l'utente vuole ricevere delle mail ad ogni risposta.", required = true)
    private boolean emailUser;
}
