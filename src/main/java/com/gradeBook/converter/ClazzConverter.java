package com.gradeBook.converter;

import com.gradeBook.entity.Clazz;
import com.gradeBook.entity.bom.ClazzBom;
import com.gradeBook.exception.EntityNotFoundException;
import com.gradeBook.repository.ClazzRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClazzConverter {
    private final ClazzRepo repo;

    public ClazzBom toBom(Clazz source) {
        if (source == null) return null;
        ClazzBom result = new ClazzBom();
        result.setOID(source.getOID());
        result.setName(source.getName());
        return result;
    }

    public List<ClazzBom> toBom(List<Clazz> source) {
        List<ClazzBom> result = new ArrayList<>();
        if (source == null) return result;
        source.forEach(clazz -> result.add(toBom(clazz)));
        return result;
    }

    public Clazz fromBom(ClazzBom source) {
        if (source == null) return null;
        Clazz result = new Clazz();
        if (source.getOID() != null) {
            Optional<Clazz> optionalClazz = repo.findById(source.getOID());
            if (optionalClazz.isEmpty()) throw new EntityNotFoundException(source.getOID());
            result = optionalClazz.get();
        }
        result.setName(source.getName());
        return result;
    }
}
