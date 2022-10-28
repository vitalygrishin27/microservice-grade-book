package com.gradeBook.service.impl;

import com.gradeBook.entity.Subject;
import com.gradeBook.exception.SubjectAlreadyExistsException;
import com.gradeBook.exception.SubjectNotFoundException;
import com.gradeBook.repository.SubjectRepo;
import com.gradeBook.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepo subjectRepo;

    public List<Subject> findAll() {
        return subjectRepo.findAll();
    }

    public Subject create(Subject subject) {
        if (subjectRepo.findByName(subject.getName()) != null)
            throw new SubjectAlreadyExistsException(subject.getName());
        return subjectRepo.saveAndFlush(subject);
    }

    public Subject update(Subject subject) {
        Subject subjectFromDB = subjectRepo.findByName(subject.getName());
        if (subjectFromDB != null && !Objects.equals(subjectFromDB.getOID(), subject.getOID()))
            throw new SubjectAlreadyExistsException(subject.getName());
        return subjectRepo.saveAndFlush(subject);
    }

    public void delete(Long subjectId) {
        Subject subject = subjectRepo.findById(subjectId).orElseThrow(() -> new SubjectNotFoundException(subjectId));
        subjectRepo.delete(subject);
    }
}
