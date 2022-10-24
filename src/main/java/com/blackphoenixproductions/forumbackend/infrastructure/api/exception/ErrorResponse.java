package com.blackphoenixproductions.forumbackend.infrastructure.api.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ErrorResponse {
    private String errorMessage;
    private HttpStatus status;


    public ErrorResponse(String errorMessage, HttpStatus status) {
        this.errorMessage = errorMessage;
        this.status = status;
    }
}
