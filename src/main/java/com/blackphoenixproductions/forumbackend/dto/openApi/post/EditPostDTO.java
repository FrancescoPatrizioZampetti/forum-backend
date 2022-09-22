package com.blackphoenixproductions.forumbackend.dto.openApi.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Il post da modificare.")
public class EditPostDTO {
    @NotNull(message = "L'id del post non puo' essere null")
    @Schema(description = "L'id del post.", required = true)
    private Long id;
    @NotBlank(message = "Il messaggio del post non può essere null/vuoto")
    @Schema(description = "Il messaggio del post.", required = true)
    private String message;

    public EditPostDTO(Long id) {
        this.id = id;
    }
}
