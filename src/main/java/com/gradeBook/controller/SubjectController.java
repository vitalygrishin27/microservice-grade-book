package com.gradeBook.controller;

import com.gradeBook.entity.AccessLevel;
import com.gradeBook.entity.Token;
import com.gradeBook.entity.bom.SubjectBom;
import com.gradeBook.exception.ForbiddenByAccessLevelException;
import com.gradeBook.service.CRUDService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("subjects")
@Slf4j
public class SubjectController {

    private final CRUDService<SubjectBom> CRUDService;
    private static final String LOG_MESSAGE_TEMPLATE = "[SubjectController.%s] [%s] [Access level = %s] [Login = %s] [Parameters: %s]";

    @GetMapping
    public ResponseEntity<List<SubjectBom>> getSubjects(@RequestAttribute AccessLevel.LEVEL level, @RequestAttribute Token token, @RequestParam(defaultValue = "true") Boolean needToSort, String search) {
        log.info(String.format(LOG_MESSAGE_TEMPLATE, "getSubjects", LocalDateTime.now(), level, token != null ? token.getUser().getLogin() : "Undefined", "needToSort=" + needToSort + ", search=" + search));
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(CRUDService.findAll(needToSort, search), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SubjectBom> create(@NonNull @ModelAttribute SubjectBom subjectBom, @RequestAttribute Token token, @RequestAttribute AccessLevel.LEVEL level) {
        log.info(String.format(LOG_MESSAGE_TEMPLATE, "create", LocalDateTime.now(), level, token != null ? token.getUser().getLogin() : "Undefined", subjectBom));
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(CRUDService.create(subjectBom), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<SubjectBom> update(@NonNull @ModelAttribute SubjectBom subjectBom, @RequestAttribute Token token, @RequestAttribute AccessLevel.LEVEL level) {
        log.info(String.format(LOG_MESSAGE_TEMPLATE, "update", LocalDateTime.now(), level, token != null ? token.getUser().getLogin() : "Undefined", subjectBom));
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(CRUDService.update(subjectBom), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@NonNull @PathVariable() Long id, @RequestAttribute Token token, @RequestAttribute AccessLevel.LEVEL level) {
        log.info(String.format(LOG_MESSAGE_TEMPLATE, "delete", LocalDateTime.now(), level, token != null ? token.getUser().getLogin() : "Undefined", id));
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        CRUDService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
