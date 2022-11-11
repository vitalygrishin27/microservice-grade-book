package com.gradeBook.converter;

import com.gradeBook.entity.Clazz;
import com.gradeBook.entity.bom.ClazzBom;
import com.gradeBook.service.impl.ClazzServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClazzConverter {
    private final ClazzServiceImpl clazzService;

    public ClazzBom toClazzBom(Clazz source) {
        if (source == null) return null;
        ClazzBom result = new ClazzBom();
        result.setOID(source.getOID());
        result.setName(source.getName());
        return result;
    }

    public List<ClazzBom> toClazzBom(List<Clazz> source) {
        List<ClazzBom> result = new ArrayList<>();
        if (source == null) return result;
        source.forEach(clazz -> result.add(toClazzBom(clazz)));
        return result;
    }

    public Clazz toClazz(ClazzBom source) {
        if (source == null) return null;
        Clazz result = clazzService.findById(source.getOID());
        if (result == null) result = new Clazz();
        result.setName(source.getName());
        return result;
    }
}
