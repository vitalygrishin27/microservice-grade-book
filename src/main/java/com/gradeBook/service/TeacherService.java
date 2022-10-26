package com.gradeBook.service;

import com.gradeBook.entity.Teacher;
import com.gradeBook.entity.Token;
import com.gradeBook.repository.TeacherRepo;
import com.gradeBook.repository.TokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepo teacherRepo;

    public Teacher save(Teacher teacher){
        return teacherRepo.saveAndFlush(teacher);
    }
}
