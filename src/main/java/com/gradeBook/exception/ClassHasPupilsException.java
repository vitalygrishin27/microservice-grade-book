package com.gradeBook.exception;

import com.gradeBook.exception.helper.GradeBookException;
import org.springframework.http.HttpStatus;

public class ClassHasPupilsException extends GradeBookException {
    public ClassHasPupilsException(String className) {
        super("GBE-ENTITY-005", String.format("Class with name = %s has pupils", className), HttpStatus.CONFLICT);
    }
}
