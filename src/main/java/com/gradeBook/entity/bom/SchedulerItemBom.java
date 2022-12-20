package com.gradeBook.entity.bom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulerItemBom {
    private SubjectBom subjectBom;
    private ClazzBom clazzBom;
}

