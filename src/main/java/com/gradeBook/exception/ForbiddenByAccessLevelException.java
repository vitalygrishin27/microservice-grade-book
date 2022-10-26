package com.gradeBook.exception;

import com.gradeBook.exception.helper.GradeBookException;
import org.springframework.http.HttpStatus;

public class ForbiddenByAccessLevelException extends GradeBookException {
    public ForbiddenByAccessLevelException(){
        super("GBE-ACCESS-001","You access level not allow to request this data.", HttpStatus.FORBIDDEN);
    }
}
