package com.haratres.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotDeletedException extends RuntimeException{
    public NotDeletedException(String message) {
        super(message);
    }

    public NotDeletedException(String message, Throwable cause) {
        super(message, cause);
    }
}
