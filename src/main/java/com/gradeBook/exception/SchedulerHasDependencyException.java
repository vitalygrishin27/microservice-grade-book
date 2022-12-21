package com.gradeBook.exception;

import com.gradeBook.exception.helper.GradeBookException;
import org.springframework.http.HttpStatus;

public class SchedulerHasDependencyException extends GradeBookException {
    public SchedulerHasDependencyException() {
        super("GBE-ENTITY-008", "User was updated with list of Subjects which not include subjects that are presented in scheduler", HttpStatus.FAILED_DEPENDENCY);
    }
}
