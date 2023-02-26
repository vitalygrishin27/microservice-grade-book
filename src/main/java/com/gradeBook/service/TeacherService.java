package com.gradeBook.service;

import com.gradeBook.converter.UserConverter;
import com.gradeBook.entity.Teacher;
import com.gradeBook.entity.bom.SubjectBom;
import com.gradeBook.entity.bom.UserBom;
import com.gradeBook.repository.TeacherRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepo teacherRepo;
    private final UserConverter userConverter;

    public Teacher save(Teacher teacher) {
        return teacherRepo.saveAndFlush(teacher);
    }

    public List<UserBom> findBySubjectBom(SubjectBom subjectBom) {
        return userConverter.toBom(new ArrayList<>(teacherRepo.findBySubjects_OID(subjectBom.getOID())));
    }

    public Teacher findById(Long oid) {
        return teacherRepo.findById(oid).orElse(null);
    }

    public List<Teacher> findAll() {
        return teacherRepo.findAll();
    }
}
