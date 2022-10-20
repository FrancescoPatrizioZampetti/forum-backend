package com.blackphoenixproductions.forumbackend.domain.dto.openApi.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Schema(description = "Il post da creare.")
public class InsertPostDTO {
    @NotEmpty(message = "Il messaggio del post non puo' essere null/vuoto")
    @Size(max = 20000, message = "Il messaggio del post ha superato il massimo di 20000 caratteri")
    @Schema(description = "Il messaggio del post.", required = true)
    private String message;
    @NotNull(message = "L'id del topic non puo' essere null")
    @Schema(description = "L'id del del topic a cui appartiene il post.", required = true)
    private Long topicId;

    public InsertPostDTO(String message, Long topicId) {
        this.message = message;
        this.topicId = topicId;
    }
}
