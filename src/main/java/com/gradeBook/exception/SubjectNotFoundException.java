package com.gradeBook.exception;

import com.gradeBook.exception.helper.GradeBookException;
import org.springframework.http.HttpStatus;

public class SubjectNotFoundException extends GradeBookException {
    public SubjectNotFoundException(Long id) {
        super("GBE-SUBJECT-002", String.format("Subject with id = %s not exists", id), HttpStatus.NOT_FOUND);
    }
}
