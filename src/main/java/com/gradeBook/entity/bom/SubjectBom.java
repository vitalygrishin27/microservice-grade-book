package com.gradeBook.entity.bom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectBom {
    private Long OID;
    private String name;
    private String schedulerInternalId;
    private List<UserBom> teachers = new ArrayList<>();
    private Long selectedTeacher;
    private List<SubjectBom> conflicts;
    private ClazzBom clazzWithConflict;
}
