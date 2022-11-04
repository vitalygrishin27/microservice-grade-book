package com.gradeBook.exception;

import com.gradeBook.exception.helper.GradeBookException;
import org.springframework.http.HttpStatus;

public class EntityAlreadyExistsException extends GradeBookException {
    public EntityAlreadyExistsException(String name) {
        super("GBE-ENTITY-001", String.format("Entity with name = %s already exists", name), HttpStatus.CONFLICT);
    }
}
