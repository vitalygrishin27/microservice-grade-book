package com.gradeBook.converter;

import com.gradeBook.entity.Subject;
import com.gradeBook.entity.bom.SubjectBom;
import com.gradeBook.exception.EntityNotFoundException;
import com.gradeBook.repository.SubjectRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubjectConverter {
    private final SubjectRepo repo;

    public SubjectBom toBom(Subject source) {
        if (source == null) return null;
        SubjectBom result = new SubjectBom();
        result.setOID(source.getOID());
        result.setName(source.getName());
        result.setSchedulerInternalId(UUID.randomUUID().toString());
        return result;
    }

    public List<SubjectBom> toBom(List<Subject> source) {
        List<SubjectBom> result = new ArrayList<>();
        if (source == null) return result;
        source.forEach(subject -> result.add(toBom(subject)));
        return result;
    }

    public Subject fromBom(SubjectBom source) {
        if (source == null) return null;
        Subject result = new Subject();
        if (source.getOID() != null) {
            Optional<Subject> optionalSubject = repo.findById(source.getOID());
            if (optionalSubject.isEmpty()) throw new EntityNotFoundException(source.getOID());
            result = optionalSubject.get();
        }
        result.setName(source.getName());
        return result;
    }
}
