package com.gradeBook.controller;

import com.gradeBook.entity.*;
import com.gradeBook.exception.ForbiddenByAccessLevelException;
import com.gradeBook.repository.PupilRepo;
import com.gradeBook.repository.TeacherRepo;
import com.gradeBook.repository.UserRepo;
import com.gradeBook.repository.WatcherRepo;
import com.gradeBook.service.AccessLevelService;
import com.gradeBook.service.TeacherService;
import com.gradeBook.service.TokenService;
import com.gradeBook.service.UserService;
import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Controller
@RequiredArgsConstructor
@RequestMapping("users")

public class UserController {

    private final UserRepo userRepo;
    private final PupilRepo pupilRepo;
    private final WatcherRepo watcherRepo;
    private final TeacherRepo teacherRepo;

    private final TokenService tokenService;
    private final UserService userService;
    private final AccessLevelService accessLevelService;
    private final TeacherService teacherService;

    @PostMapping("/token")
    public ResponseEntity<Token> createToken(@NotNull @RequestBody Watcher requestedUser) {
        return new ResponseEntity<>(tokenService.create(userService.getUserIfLoginAndPassIsCorrect(requestedUser.getLogin(), requestedUser.getPassword())), HttpStatus.OK);
    }

    @GetMapping("/token")
    public ResponseEntity<Token> checkToken(@Nullable @RequestAttribute Token token, @RequestAttribute AccessLevel.LEVEL level) {
        if (token == null || !token.isValid()) throw new ForbiddenByAccessLevelException();
        token.setUsername(token.getUser().getLogin());
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers(@NotNull @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("pupils")
    public ResponseEntity<List<Pupil>> getPupils(@NotNull @RequestAttribute AccessLevel.LEVEL level) {
        return new ResponseEntity<>(pupilRepo.findAll(), HttpStatus.OK);
    }

    @GetMapping("teachers")
    public ResponseEntity<List<Teacher>> getTeachers(@NotNull @RequestAttribute AccessLevel.LEVEL level) {
        return new ResponseEntity<>(teacherRepo.findAll(), HttpStatus.OK);
    }

    @GetMapping("watchers")
    public ResponseEntity<List<Watcher>> getWatchers(@NotNull @RequestAttribute AccessLevel.LEVEL level) {
        return new ResponseEntity<>(watcherRepo.findAll(), HttpStatus.OK);
    }

    @PostMapping("teachers")
    public ResponseEntity<User> createTeacher(@RequestBody Teacher teacher, @NotNull @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(userRepo.saveAndFlush(teacher), HttpStatus.OK);
    }

    @PostMapping("watchers")
    public ResponseEntity<User> createWatcher(@RequestBody Watcher watcher, @NotNull @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(userRepo.saveAndFlush(watcher), HttpStatus.OK);
    }

    @PostMapping("pupils")
    public ResponseEntity<User> createPupil(@RequestBody Pupil pupil, @NotNull @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(userRepo.saveAndFlush(pupil), HttpStatus.OK);
    }

    @PutMapping("teachers")
    public ResponseEntity<User> updateTeacher(@RequestBody Teacher teacher, @NotNull @RequestAttribute AccessLevel.LEVEL level) {
        return new ResponseEntity<>(userRepo.saveAndFlush(teacher), HttpStatus.OK);
    }

    @PutMapping("watchers")
    public ResponseEntity<User> updateWatcher(@RequestBody Watcher watcher, @NotNull @RequestAttribute AccessLevel.LEVEL level) {
        return new ResponseEntity<>(userRepo.saveAndFlush(watcher), HttpStatus.OK);
    }

    @PutMapping("pupils")
    public ResponseEntity<User> updatePupil(@RequestBody Pupil pupil, @NotNull @RequestAttribute AccessLevel.LEVEL level) {
        return new ResponseEntity<>(userRepo.saveAndFlush(pupil), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> updateTeacher(@PathVariable Long id, @NotNull @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        userRepo.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
