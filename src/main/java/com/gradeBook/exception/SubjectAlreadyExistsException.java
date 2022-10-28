package com.gradeBook.exception;

import com.gradeBook.exception.helper.GradeBookException;
import org.springframework.http.HttpStatus;

public class SubjectAlreadyExistsException extends GradeBookException {
    public SubjectAlreadyExistsException(String name) {
        super("GBE-SUBJECT-001", String.format("Subject with name = %s already exists", name), HttpStatus.CONFLICT);
    }
}
