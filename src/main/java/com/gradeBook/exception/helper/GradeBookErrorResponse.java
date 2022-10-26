package com.gradeBook.exception.helper;

import lombok.Data;
@Data
public class GradeBookErrorResponse {
    private String errorCode;
    private String errorMessage;
}
