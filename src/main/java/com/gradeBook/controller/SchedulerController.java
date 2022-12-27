package com.gradeBook.controller;

import com.gradeBook.entity.AccessLevel;
import com.gradeBook.entity.Token;
import com.gradeBook.entity.bom.SchedulerBom;
import com.gradeBook.entity.bom.SchedulerItemBom;
import com.gradeBook.exception.ForbiddenByAccessLevelException;
import com.gradeBook.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import java.util.LinkedHashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("schedulers")
@Slf4j
public class SchedulerController {

    private final SchedulerService schedulerService;
    private static final String LOG_MESSAGE_TEMPLATE = "[%s] [Access level = %s] [Login = %s] [Parameters: %s]";

    @GetMapping("/classes/{id}")
    public ResponseEntity<SchedulerBom> getSchedulerByClass(@RequestAttribute AccessLevel.LEVEL level, @RequestAttribute Token token, @PathVariable() Long id) {
        log.info(String.format(LOG_MESSAGE_TEMPLATE, "getSchedulerByClass", level, token != null ? token.getUser().getLogin() : "Undefined", id));
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(schedulerService.getScheduler(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SchedulerBom> create(@NonNull @RequestBody SchedulerBom schedulerBom, @RequestAttribute Token token, @RequestAttribute AccessLevel.LEVEL level) {
        log.info(String.format(LOG_MESSAGE_TEMPLATE, "create", level, token != null ? token.getUser().getLogin() : "Undefined", schedulerBom));
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        schedulerService.save(schedulerBom);
        return new ResponseEntity<>(schedulerService.getScheduler(schedulerBom.getClazz().getOID()), HttpStatus.OK);
    }

    @GetMapping("/teachers")
    public ResponseEntity<LinkedHashMap<String, List<SchedulerItemBom>>> getSchedulerByTeacher(@RequestAttribute Token token, @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.TEACHER.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(schedulerService.getSchedulerByTeacher(token.getUser()), HttpStatus.OK);
    }

  /*  @PutMapping
    public ResponseEntity<SubjectBom> update(@NonNull @ModelAttribute SubjectBom subjectBom, @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(CRUDService.update(subjectBom), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@NonNull @PathVariable() Long id, @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        CRUDService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }*/
}
