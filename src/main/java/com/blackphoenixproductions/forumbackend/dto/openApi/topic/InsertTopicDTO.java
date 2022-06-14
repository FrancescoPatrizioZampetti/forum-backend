package com.blackphoenixproductions.forumbackend.dto.openApi.topic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Il topic da creare.")
public class InsertTopicDTO {
    @Schema(description = "Il titolo del topic", required = true)
    private String title;
    @Schema(description = "Il messaggio del topic.", required = true)
    private String message;
    @Schema(description = "Indica se dovrà essere messo in evidenza.", required = true)
    private boolean pinned;
    @Schema(description = "Indica se l'utente vuole ricevere delle mail ad ogni risposta.", required = true)
    private boolean emailUser;
}
