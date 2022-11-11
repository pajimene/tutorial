package com.ccsw.tutorial.client.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Same name")
public class IncorrectNameException extends Exception {

    public IncorrectNameException(String errorMessage) {
        super(errorMessage);
    }

}
