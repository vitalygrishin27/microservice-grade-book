package com.gradeBook.exception.helper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GradeBookExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GradeBookException.class)
    public ResponseEntity<GradeBookErrorResponse> handleError(GradeBookException ex, WebRequest request)  {
        GradeBookErrorResponse response = new GradeBookErrorResponse();
        response.setErrorMessage(ex.getErrorMessage());
        response.setErrorCode(ex.getErrorCode());
        return new ResponseEntity<>(response, ex.getHttpStatus());
    }
}
