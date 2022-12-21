package com.gradeBook.service.impl;

import com.gradeBook.converter.ClazzConverter;
import com.gradeBook.entity.Clazz;
import com.gradeBook.entity.bom.ClazzBom;
import com.gradeBook.exception.ClassHasPupilsException;
import com.gradeBook.exception.EntityAlreadyExistsException;
import com.gradeBook.exception.EntityIsInvalidException;
import com.gradeBook.exception.EntityNotFoundException;
import com.gradeBook.repository.ClazzRepo;
import com.gradeBook.repository.PupilRepo;
import com.gradeBook.service.CRUDService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClazzServiceImpl implements CRUDService<ClazzBom> {

    private final ClazzRepo clazzRepo;
    private final PupilRepo pupilRepo;
    private final ClazzConverter clazzConverter;

    public List<ClazzBom> findAll(Boolean needToSort, String search) {
        List<ClazzBom> result = clazzConverter.toBom(clazzRepo.findAll());
        if (!search.isEmpty()) result = filterResult(result, search);
        if (!needToSort) return result;
        return result.stream().sorted(Comparator.comparing(ClazzBom::getName)).collect(Collectors.toList());
    }

    public ClazzBom findById(Long id) {
        Optional<Clazz> clazzOptional = clazzRepo.findById(id);
        if (clazzOptional.isEmpty()) return null;
        return clazzConverter.toBom(clazzOptional.get());
    }

    public Clazz findClazzById(Long id) {
        Optional<Clazz> clazzOptional = clazzRepo.findById(id);
        return clazzOptional.orElse(null);
    }

    public ClazzBom create(ClazzBom clazzBom) {
        if (clazzBom.getName().isBlank()) throw new EntityIsInvalidException();
        replaceCyrillicSymbols(clazzBom);
        if (clazzRepo.findByName(clazzBom.getName()) != null)
            throw new EntityAlreadyExistsException(clazzBom.getName());
        return clazzConverter.toBom(clazzRepo.saveAndFlush(clazzConverter.fromBom(clazzBom)));
    }

    public ClazzBom update(ClazzBom clazzBom) {
        if (clazzBom.getName().isBlank()) throw new EntityIsInvalidException();
        replaceCyrillicSymbols(clazzBom);
        Clazz clazzFromDB = clazzRepo.findByName(clazzBom.getName());
        if (clazzFromDB != null && !Objects.equals(clazzFromDB.getOID(), clazzBom.getOID()))
            throw new EntityAlreadyExistsException(clazzBom.getName());
        return clazzConverter.toBom(clazzRepo.saveAndFlush(clazzConverter.fromBom(clazzBom)));
    }

    @Transactional
    public void delete(Long id) {
        Clazz clazz = clazzRepo.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        if (!pupilRepo.findByClazz(clazz).isEmpty()) throw new ClassHasPupilsException(clazz.getName());
        clazzRepo.delete(clazz);
    }

    private void replaceCyrillicSymbols(ClazzBom clazzBom) {
        clazzBom.setName(clazzBom.getName().replace("А", "A"));
        clazzBom.setName(clazzBom.getName().replace("В", "B"));
        clazzBom.setName(clazzBom.getName().replace("С", "C"));
        clazzBom.setName(clazzBom.getName().replace("Е", "E"));
        clazzBom.setName(clazzBom.getName().replace("а", "a"));
        clazzBom.setName(clazzBom.getName().replace("в", "b"));
        clazzBom.setName(clazzBom.getName().replace("с", "c"));
        clazzBom.setName(clazzBom.getName().replace("е", "e"));
    }

    private List<ClazzBom> filterResult(List<ClazzBom> clazzes, String search) {
        return clazzes.stream().filter(clazz ->
                clazz.getName().toLowerCase().contains(search.toLowerCase())).collect(Collectors.toList());
    }
}
