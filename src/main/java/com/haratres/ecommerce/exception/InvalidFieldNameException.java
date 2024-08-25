package com.haratres.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidFieldNameException extends RuntimeException {
    public InvalidFieldNameException(String message) {
        super(message);
    }

    public InvalidFieldNameException(String message, Throwable cause) {
        super(message, cause);
    }
}
