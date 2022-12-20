package com.gradeBook.controller;

import com.gradeBook.entity.AccessLevel;
import com.gradeBook.entity.Token;
import com.gradeBook.entity.bom.SchedulerBom;
import com.gradeBook.exception.ForbiddenByAccessLevelException;
import com.gradeBook.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("schedulers")
@Slf4j
public class SchedulerController {

    private final SchedulerService schedulerService;
    private static final String LOG_MESSAGE_TEMPLATE = "[SchedulerController.%s] [%s] [Access level = %s] [Login = %s] [Parameters: %s]";

    @GetMapping("/classes/{id}")
    public ResponseEntity<SchedulerBom> getSchedulerByClass(@RequestAttribute AccessLevel.LEVEL level, @RequestAttribute Token token, @PathVariable() Long id) {
        log.info(String.format(LOG_MESSAGE_TEMPLATE, "getSchedulerByClass", LocalDateTime.now(), level, token != null ? token.getUser().getLogin() : "Undefined", id));
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(schedulerService.getScheduler(id), HttpStatus.OK);
    }

    @PostMapping
    public String create(@NonNull @RequestBody SchedulerBom schedulerBom, @RequestAttribute Token token, @RequestAttribute AccessLevel.LEVEL level) {
        log.info(String.format(LOG_MESSAGE_TEMPLATE, "create", LocalDateTime.now(), level, token != null ? token.getUser().getLogin() : "Undefined", schedulerBom));
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        schedulerService.save(schedulerBom);
        return "Done";
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
