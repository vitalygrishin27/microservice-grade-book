package com.gradeBook.controller;

import com.gradeBook.entity.*;
import com.gradeBook.exception.ForbiddenByAccessLevelException;
import com.gradeBook.repository.PupilRepo;
import com.gradeBook.repository.TeacherRepo;
import com.gradeBook.repository.UserRepo;
import com.gradeBook.repository.WatcherRepo;
import com.gradeBook.service.*;
import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("subjects")

public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping
    public ResponseEntity<List<Subject>> getSubjects(@RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(subjectService.findAll(), HttpStatus.OK);
    }

}
