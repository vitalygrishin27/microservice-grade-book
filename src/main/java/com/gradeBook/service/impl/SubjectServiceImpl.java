package com.gradeBook.service.impl;

import com.gradeBook.converter.SubjectConverter;
import com.gradeBook.entity.Subject;
import com.gradeBook.entity.bom.SubjectBom;
import com.gradeBook.exception.EntityAlreadyExistsException;
import com.gradeBook.exception.EntityIsInvalidException;
import com.gradeBook.exception.EntityNotFoundException;
import com.gradeBook.repository.SubjectRepo;
import com.gradeBook.service.CRUDService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements CRUDService<SubjectBom> {

    private final SubjectRepo subjectRepo;
    private final SubjectConverter subjectConverter;

    public List<SubjectBom> findAll(Boolean needToSort) {
        List<Subject> result = subjectRepo.findAll();
        if (!needToSort) return subjectConverter.toBom(result);
        return subjectConverter.toBom(result.stream().sorted(Comparator.comparing(Subject::getName)).collect(Collectors.toList()));
    }

    public SubjectBom create(SubjectBom subjectBom) {
        if (subjectBom.getName().equals("")) throw new EntityIsInvalidException();
        if (subjectRepo.findByName(subjectBom.getName()) != null)
            throw new EntityAlreadyExistsException(subjectBom.getName());
        return subjectConverter.toBom(subjectRepo.saveAndFlush(subjectConverter.fromBom(subjectBom)));
    }

    public SubjectBom update(SubjectBom subjectBom) {
        if (subjectBom.getName().equals("")) throw new EntityIsInvalidException();
        Subject subjectFromDB = subjectRepo.findByName(subjectBom.getName());
        if (subjectFromDB != null && !Objects.equals(subjectFromDB.getOID(), subjectBom.getOID()))
            throw new EntityAlreadyExistsException(subjectBom.getName());
        return subjectConverter.toBom(subjectRepo.saveAndFlush(subjectConverter.fromBom(subjectBom)));
    }

    public void delete(Long id) {
        Subject subject = subjectRepo.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        subjectRepo.delete(subject);
    }
}
