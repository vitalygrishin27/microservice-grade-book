package com.gradeBook.exception;

import com.gradeBook.exception.helper.GradeBookException;
import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends GradeBookException {
    public EntityNotFoundException(Long id) {
        super("GBE-ENTITY-002", String.format("Entity with id = %s not exists", id), HttpStatus.NOT_FOUND);
    }
}
