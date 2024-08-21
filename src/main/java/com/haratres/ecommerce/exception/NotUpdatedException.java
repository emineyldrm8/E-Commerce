package com.haratres.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotUpdatedException extends RuntimeException{
    public NotUpdatedException(String message) {
        super(message);
    }

    public NotUpdatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
