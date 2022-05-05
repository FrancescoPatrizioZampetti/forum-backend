package com.blackphoenixproductions.forumbackend.dto.openApi.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Il post da modificare.")
public class EditPostDTO {

    @Schema(description = "L'id del post.", required = true)
    private Long id;
    @Schema(description = "Il messaggio del post.", required = true)
    private String message;

    public EditPostDTO(Long id) {
        this.id = id;
    }
}
