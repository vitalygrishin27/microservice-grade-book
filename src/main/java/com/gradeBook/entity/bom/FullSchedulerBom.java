package com.gradeBook.entity.bom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullSchedulerBom {
    private UserBom teacherBom;
    private List<List<SchedulerItemBom>> itemList;
}

