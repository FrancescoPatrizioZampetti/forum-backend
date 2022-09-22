package com.blackphoenixproductions.forumbackend.dto.openApi.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Schema(description = "Il post da creare.")
public class InsertPostDTO {
    @NotBlank(message = "Il messaggio del post non può essere null/vuoto")
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
