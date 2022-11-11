package com.ccsw.tutorial.loan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Final date cant be before than initial")
public class FinalBeforeInitialException extends Exception {

    public FinalBeforeInitialException(String errorMessage) {
        super(errorMessage);
    }
}
