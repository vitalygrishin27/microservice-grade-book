package com.gradeBook.exception;

import com.gradeBook.exception.helper.GradeBookException;
import org.springframework.http.HttpStatus;

public class ClassAlreadyHasFormMasterException extends GradeBookException {
    public ClassAlreadyHasFormMasterException(String className, String lastName) {
        super("GBE-ENTITY-004", String.format("Class with name = %s already has form master %s", className, lastName), HttpStatus.CONFLICT);
    }
}
