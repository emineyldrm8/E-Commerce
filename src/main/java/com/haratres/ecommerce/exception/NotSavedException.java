package com.haratres.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotSavedException extends RuntimeException{
    public NotSavedException(String message) {
        super(message);
    }

    public NotSavedException(String message, Throwable cause) {
        super(message, cause);
    }
}
