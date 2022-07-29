package com.gucardev.collaborativewebbe.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class GeneralException extends RuntimeException{
    private String message;
    private HttpStatus status;

    public GeneralException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

}
