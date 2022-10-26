package com.gradeBook.exception;

import com.gradeBook.exception.helper.GradeBookException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends GradeBookException {
    public UserNotFoundException(String login) {
        super("GBE-USER-001", String.format("User with Login = %s not exists", login), HttpStatus.NOT_FOUND);
    }
}
