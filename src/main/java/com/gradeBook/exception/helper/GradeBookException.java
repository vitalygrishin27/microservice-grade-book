package com.gradeBook.exception.helper;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class GradeBookException extends RuntimeException {
    private String errorMessage;
    private String errorCode;
    private HttpStatus httpStatus;

    public GradeBookException(String errorCode, String errorMessage, HttpStatus httpStatus) {
        super();
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }
}
