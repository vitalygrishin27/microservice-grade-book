package com.gradeBook.controller;

import com.gradeBook.entity.AccessLevel;
import com.gradeBook.entity.Subject;
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

    private final CRUDService<Subject> CRUDService;

    @GetMapping
    public ResponseEntity<List<Subject>> getSubjects(@RequestAttribute AccessLevel.LEVEL level, @RequestParam(defaultValue = "true") Boolean needToSort) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(CRUDService.findAll(needToSort), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Subject> create(@NonNull @ModelAttribute Subject subject, @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(CRUDService.create(subject), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Subject> update(@NonNull @ModelAttribute Subject subject, @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(CRUDService.update(subject), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@NonNull @PathVariable() Long id, @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        CRUDService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
