package com.ccsw.tutorial.loan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Client has a loan")
public class MaxLoansException extends Exception {

    public MaxLoansException(String errorMessage) {
        super(errorMessage);
    }
}
