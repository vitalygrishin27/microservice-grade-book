package com.gradeBook.entity.bom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulerBom {
    private ClazzBom clazz;
    private ArrayList<LinkedHashMap<String, Object>> daySchedulerBomList;
}
