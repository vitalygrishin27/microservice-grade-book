package com.gradeBook.service.impl;

import com.gradeBook.entity.Clazz;
import com.gradeBook.exception.EntityAlreadyExistsException;
import com.gradeBook.exception.EntityIsInvalidException;
import com.gradeBook.exception.EntityNotFoundException;
import com.gradeBook.repository.ClazzRepo;
import com.gradeBook.service.CRUDService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ClazzServiceImpl implements CRUDService<Clazz> {

    private final ClazzRepo clazzRepo;

    public List<Clazz> findAll() {
        return clazzRepo.findAll();
    }

    public Clazz create(Clazz clazz) {
        if (clazz.getName().equals("")) throw new EntityIsInvalidException();
        if (clazzRepo.findByName(clazz.getName()) != null)
            throw new EntityAlreadyExistsException(clazz.getName());
        return clazzRepo.saveAndFlush(clazz);
    }

    public Clazz update(Clazz clazz) {
        if (clazz.getName().equals("")) throw new EntityIsInvalidException();
        Clazz clazzFromDB = clazzRepo.findByName(clazz.getName());
        if (clazzFromDB != null && !Objects.equals(clazzFromDB.getOID(), clazz.getOID()))
            throw new EntityAlreadyExistsException(clazz.getName());
        return clazzRepo.saveAndFlush(clazz);
    }

    public void delete(Long id) {
        Clazz clazz = clazzRepo.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        clazzRepo.delete(clazz);
    }
}
