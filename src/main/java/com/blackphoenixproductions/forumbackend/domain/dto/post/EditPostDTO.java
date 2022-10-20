package com.blackphoenixproductions.forumbackend.domain.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Il post da modificare.")
public class EditPostDTO {
    @NotNull(message = "L'id del post non puo' essere null")
    @Schema(description = "L'id del post.", required = true)
    private Long id;
    @NotEmpty(message = "Il messaggio del post non puo' essere null/vuoto")
    @Size(max = 20000, message = "Il messaggio del post ha superato il massimo di 20000 caratteri")
    @Schema(description = "Il messaggio del post.", required = true)
    private String message;

    public EditPostDTO(Long id) {
        this.id = id;
    }
}
