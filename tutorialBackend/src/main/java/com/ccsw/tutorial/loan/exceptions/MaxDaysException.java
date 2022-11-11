package com.ccsw.tutorial.loan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "More than 14 days")
public class MaxDaysException extends Exception {
    public MaxDaysException(String errorMessage) {
        super(errorMessage);
    }
}
