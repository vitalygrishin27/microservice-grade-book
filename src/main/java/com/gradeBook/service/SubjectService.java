package com.gradeBook.service;

import com.gradeBook.entity.Subject;
import com.gradeBook.entity.User;
import com.gradeBook.exception.InvalidUserPasswordException;
import com.gradeBook.exception.UserNotFoundException;
import com.gradeBook.repository.SubjectRepo;
import com.gradeBook.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepo subjectRepo;

    public List<Subject> findAll() {
        return subjectRepo.findAll();
    }

}
