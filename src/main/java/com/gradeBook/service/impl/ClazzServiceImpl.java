package com.gradeBook.service.impl;

import com.gradeBook.entity.Clazz;
import com.gradeBook.exception.EntityAlreadyExistsException;
import com.gradeBook.exception.EntityIsInvalidException;
import com.gradeBook.exception.EntityNotFoundException;
import com.gradeBook.repository.ClazzRepo;
import com.gradeBook.service.CRUDService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClazzServiceImpl implements CRUDService<Clazz> {

    private final ClazzRepo clazzRepo;

    public List<Clazz> findAll(Boolean needToSort) {
        List<Clazz> result = clazzRepo.findAll();
        if (!needToSort) return result;
        return result.stream().sorted(Comparator.comparing(Clazz::getName)).collect(Collectors.toList());
    }

    public Clazz findById(Long id) {
        return clazzRepo.findById(id).get();
    }

    public Clazz create(Clazz clazz) {
        if (clazz.getName().equals("")) throw new EntityIsInvalidException();
        replaceCyrillicSymbols(clazz);
        if (clazzRepo.findByName(clazz.getName()) != null)
            throw new EntityAlreadyExistsException(clazz.getName());
        return clazzRepo.saveAndFlush(clazz);
    }

    public Clazz update(Clazz clazz) {
        if (clazz.getName().equals("")) throw new EntityIsInvalidException();
        replaceCyrillicSymbols(clazz);
        Clazz clazzFromDB = clazzRepo.findByName(clazz.getName());
        if (clazzFromDB != null && !Objects.equals(clazzFromDB.getOID(), clazz.getOID()))
            throw new EntityAlreadyExistsException(clazz.getName());
        return clazzRepo.saveAndFlush(clazz);
    }

    public void delete(Long id) {
        Clazz clazz = clazzRepo.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        clazzRepo.delete(clazz);
    }

    private void replaceCyrillicSymbols(Clazz clazz) {
        clazz.setName(clazz.getName().replace("А", "A"));
        clazz.setName(clazz.getName().replace("В", "B"));
        clazz.setName(clazz.getName().replace("С", "C"));
        clazz.setName(clazz.getName().replace("Е", "E"));
        clazz.setName(clazz.getName().replace("а", "a"));
        clazz.setName(clazz.getName().replace("в", "b"));
        clazz.setName(clazz.getName().replace("с", "c"));
        clazz.setName(clazz.getName().replace("е", "e"));
    }
}
