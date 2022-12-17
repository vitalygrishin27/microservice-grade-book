package com.gradeBook.entity.bom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectBom {
    private Long OID;
    private String name;
    private String schedulerInternalId;
}
