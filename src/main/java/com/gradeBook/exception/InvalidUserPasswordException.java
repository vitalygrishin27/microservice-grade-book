package com.gradeBook.exception;

import com.gradeBook.exception.helper.GradeBookException;
import org.springframework.http.HttpStatus;

public class InvalidUserPasswordException extends GradeBookException {
    public InvalidUserPasswordException(String login) {
        super("GBE-USER-002", String.format("Invalid password for User with Login = %s", login), HttpStatus.FORBIDDEN);
    }
}
