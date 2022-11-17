package com.gradeBook.controller;

import com.gradeBook.entity.AccessLevel;
import com.gradeBook.entity.bom.ClazzBom;
import com.gradeBook.exception.ForbiddenByAccessLevelException;
import com.gradeBook.service.CRUDService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("classes")
public class ClazzController {

    private final CRUDService<ClazzBom> сlazzService;

    @GetMapping
    public ResponseEntity<List<ClazzBom>> getClasses(@RequestAttribute AccessLevel.LEVEL level, @RequestParam(defaultValue = "true") Boolean needToSort, String search) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(сlazzService.findAll(needToSort, search), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ClazzBom> create(@NonNull @ModelAttribute ClazzBom clazzBom, @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(сlazzService.create(clazzBom), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ClazzBom> update(@NonNull @ModelAttribute ClazzBom clazzBom, @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(сlazzService.update(clazzBom), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@NonNull @PathVariable() Long id, @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        сlazzService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
