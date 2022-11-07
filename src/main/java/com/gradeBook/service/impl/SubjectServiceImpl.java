package com.gradeBook.service.impl;

import com.gradeBook.entity.Subject;
import com.gradeBook.exception.EntityAlreadyExistsException;
import com.gradeBook.exception.EntityIsInvalidException;
import com.gradeBook.exception.EntityNotFoundException;
import com.gradeBook.repository.SubjectRepo;
import com.gradeBook.service.CRUDService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements CRUDService<Subject> {

    private final SubjectRepo subjectRepo;

    public List<Subject> findAll() {
        return subjectRepo.findAll();
    }

    public Subject create(Subject subject) {
        if (subject.getName().equals("")) throw new EntityIsInvalidException();
        if (subjectRepo.findByName(subject.getName()) != null)
            throw new EntityAlreadyExistsException(subject.getName());
        return subjectRepo.saveAndFlush(subject);
    }

    public Subject update(Subject subject) {
        if (subject.getName().equals("")) throw new EntityIsInvalidException();
        Subject subjectFromDB = subjectRepo.findByName(subject.getName());
        if (subjectFromDB != null && !Objects.equals(subjectFromDB.getOID(), subject.getOID()))
            throw new EntityAlreadyExistsException(subject.getName());
        return subjectRepo.saveAndFlush(subject);
    }

    public void delete(Long id) {
        Subject subject = subjectRepo.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        subjectRepo.delete(subject);
    }
}
