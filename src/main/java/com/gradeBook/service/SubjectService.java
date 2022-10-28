package com.gradeBook.service;

import com.gradeBook.entity.Subject;

import java.util.List;


public interface SubjectService {
    List<Subject> findAll();

    Subject create(Subject subject);

    Subject update(Subject subject);

    void delete(Long subjectId);
}
