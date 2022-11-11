package com.ccsw.tutorial.loan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Game its in a loan within the dates")
public class GameInLoanException extends Exception {
    public GameInLoanException(String errorMessage) {
        super(errorMessage);
    }
}
