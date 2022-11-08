package com.gradeBook.controller;

import com.gradeBook.entity.*;
import com.gradeBook.entity.bom.UserBom;
import com.gradeBook.exception.ForbiddenByAccessLevelException;
import com.gradeBook.repository.UserRepo;
import com.gradeBook.service.AccessLevelService;
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

    private final TokenService tokenService;
    private final UserService userService;
    private final AccessLevelService accessLevelService;

    @PostMapping("/token")
    public ResponseEntity<Token> createToken(@NotNull @RequestBody Watcher requestedUser) {
        return new ResponseEntity<>(tokenService.create(userService.getUserIfLoginAndPassIsCorrect(requestedUser.getLogin(), requestedUser.getPassword())), HttpStatus.OK);
    }

    @GetMapping("/token")
    public ResponseEntity<Token> checkToken(@Nullable @RequestAttribute Token token, @RequestAttribute AccessLevel.LEVEL level) {
        if (token == null || !token.isValid()) throw new ForbiddenByAccessLevelException();
        token.setFirstName(token.getUser().getFirstName());
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserBom>> getUsers(@NotNull @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/pupils")
    public ResponseEntity<List<UserBom>> getPupils(@NotNull @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(userService.findAll(AccessLevel.LEVEL.PUPIL), HttpStatus.OK);
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<UserBom>> getTeachers(@NotNull @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(userService.findAll(AccessLevel.LEVEL.TEACHER), HttpStatus.OK);
    }

    @GetMapping("/admins")
    public ResponseEntity<List<UserBom>> getWatchers(@NotNull @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(userService.findAll(AccessLevel.LEVEL.ADMIN), HttpStatus.OK);
    }

    @GetMapping("/accessLevels")
    public ResponseEntity<List<AccessLevel>> getAccessLevels(@NotNull @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(accessLevelService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/teachers")
    public ResponseEntity<User> createTeacher(@RequestBody Teacher teacher, @NotNull @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(userRepo.saveAndFlush(teacher), HttpStatus.OK);
    }

    @PostMapping("/admins")
    public ResponseEntity<User> createWatcher(@RequestBody Watcher watcher, @NotNull @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(userRepo.saveAndFlush(watcher), HttpStatus.OK);
    }

    @PostMapping("/pupils")
    public ResponseEntity<User> createPupil(@RequestBody Pupil pupil, @NotNull @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(userRepo.saveAndFlush(pupil), HttpStatus.OK);
    }

    @PutMapping("/teachers")
    public ResponseEntity<User> updateTeacher(@RequestBody Teacher teacher, @NotNull @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(userRepo.saveAndFlush(teacher), HttpStatus.OK);
    }

    @PutMapping("/admins")
    public ResponseEntity<User> updateWatcher(@RequestBody Watcher watcher, @NotNull @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(userRepo.saveAndFlush(watcher), HttpStatus.OK);
    }

    @PutMapping("/pupils")
    public ResponseEntity<User> updatePupil(@RequestBody Pupil pupil, @NotNull @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(userRepo.saveAndFlush(pupil), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> updateTeacher(@PathVariable Long id, @NotNull @RequestAttribute AccessLevel.LEVEL level) {
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        userRepo.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
