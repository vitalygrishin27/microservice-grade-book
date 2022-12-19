package com.gradeBook.exception;

import com.gradeBook.exception.helper.GradeBookException;
import org.springframework.http.HttpStatus;

public class EntityHasDependencyException extends GradeBookException {
    public EntityHasDependencyException() {
        super("GBE-ENTITY-007", "Entity cannot be deleted because some dependency presents in DB", HttpStatus.FAILED_DEPENDENCY);
    }
}
