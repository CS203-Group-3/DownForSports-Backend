// to be implemented to replace current code.

package com.example.cs203g1t3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND) // 404 Error
public class FacilityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FacilityNotFoundException(Long id) {
        super("Could not find facility " + id);
    }
    

}
