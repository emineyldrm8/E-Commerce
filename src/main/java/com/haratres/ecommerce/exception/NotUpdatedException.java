package com.haratres.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotUpdatedException extends RuntimeException{
    public NotUpdatedException(String message) {
        super(message);
    }

    public NotUpdatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
