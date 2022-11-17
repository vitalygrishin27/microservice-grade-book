package com.gradeBook.controller;

import com.gradeBook.entity.AccessLevel;
import com.gradeBook.entity.bom.SubjectBom;
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
@RequestMapping("subjects")
public class SubjectController {

    private final CRUDService<SubjectBom> CRUDService;

    @GetMapping
    public ResponseEntity<List<SubjectBom>> getSubjects(@RequestAttribute AccessLevel.LEVEL level, @RequestParam(defaultValue = "true") Boolean needToSort, String search) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(CRUDService.findAll(needToSort, search), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SubjectBom> create(@NonNull @ModelAttribute SubjectBom subjectBom, @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(CRUDService.create(subjectBom), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<SubjectBom> update(@NonNull @ModelAttribute SubjectBom subjectBom, @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(CRUDService.update(subjectBom), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@NonNull @PathVariable() Long id, @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        CRUDService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
