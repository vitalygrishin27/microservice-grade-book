package com.gradeBook.controller;

import com.gradeBook.entity.AccessLevel;
import com.gradeBook.entity.Clazz;
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

    private final CRUDService<Clazz> сlazzService;

    @GetMapping
    public ResponseEntity<List<Clazz>> getClasses(@RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(сlazzService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Clazz> create(@NonNull @ModelAttribute Clazz clazz, @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(сlazzService.create(clazz), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Clazz> update(@NonNull @ModelAttribute Clazz clazz, @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(сlazzService.update(clazz), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@NonNull @PathVariable() Long id, @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        сlazzService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
