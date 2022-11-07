package com.gradeBook.exception;

import com.gradeBook.exception.helper.GradeBookException;
import org.springframework.http.HttpStatus;

public class EntityIsInvalidException extends GradeBookException {
    public EntityIsInvalidException() {
        super("GBE-ENTITY-003", "Entity is invalid", HttpStatus.CONFLICT);
    }
}
