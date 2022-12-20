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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@Controller
@RequiredArgsConstructor
@RequestMapping("users")
@Slf4j
public class UserController {

    private final UserRepo userRepo;

    private final TokenService tokenService;
    private final UserService userService;
    private final AccessLevelService accessLevelService;

    private static final String LOG_MESSAGE_TEMPLATE = "[UserController.%s] [%s] [Access level = %s] [Login = %s] [Parameters: %s]";

    @PostMapping("/token")
    public ResponseEntity<Token> createToken(@NotNull @RequestBody Watcher requestedUser) {
        log.info(String.format(LOG_MESSAGE_TEMPLATE, "createToken", LocalDateTime.now(), null, null, requestedUser));
        return new ResponseEntity<>(tokenService.create(userService.getUserIfLoginAndPassIsCorrect(requestedUser.getLogin(), requestedUser.getPassword())), HttpStatus.OK);
    }

    @GetMapping("/token")
    public ResponseEntity<Token> checkToken(@Nullable @RequestAttribute Token token, @RequestAttribute AccessLevel.LEVEL level) {
        log.info(String.format(LOG_MESSAGE_TEMPLATE, "checkToken", LocalDateTime.now(), level, (token != null && token.getUser() != null ? token.getUser().getLogin() : "Undefined"), (token != null ? token.getToken() : "Undefined")));
        if (token == null || !token.isValid()) throw new ForbiddenByAccessLevelException();
        token.setFirstName(token.getUser().getFirstName());
        token.setAccessLevel(token.getUser().getAccessLevel().getLevel().toString());
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserBom>> getUsers(@NotNull @RequestAttribute AccessLevel.LEVEL level, @RequestAttribute Token token, @RequestParam(defaultValue = "true") Boolean needToSort, String search) {
        log.info(String.format(LOG_MESSAGE_TEMPLATE, "getUsers", LocalDateTime.now(), level, token != null ? token.getUser().getLogin() : "Undefined", "needToSort=" + needToSort + ", search=" + search));
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(userService.findAll(needToSort, search), HttpStatus.OK);
    }

    @GetMapping("/pupils")
    public ResponseEntity<List<UserBom>> getPupils(@NotNull @RequestAttribute AccessLevel.LEVEL level, @RequestAttribute Token token, @RequestParam(defaultValue = "true") Boolean needToSort, String search) {
        log.info(String.format(LOG_MESSAGE_TEMPLATE, "getPupils", LocalDateTime.now(), level, token != null ? token.getUser().getLogin() : "Undefined", "needToSort=" + needToSort + ", search=" + search));
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(userService.findAll(AccessLevel.LEVEL.PUPIL, needToSort, search), HttpStatus.OK);
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<UserBom>> getTeachers(@NotNull @RequestAttribute AccessLevel.LEVEL level, @RequestAttribute Token token, @RequestParam(defaultValue = "true") Boolean needToSort, String search) {
        log.info(String.format(LOG_MESSAGE_TEMPLATE, "getTeachers", LocalDateTime.now(), level, token != null ? token.getUser().getLogin() : "Undefined", "needToSort=" + needToSort + ", search=" + search));
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(userService.findAll(AccessLevel.LEVEL.TEACHER, needToSort, search), HttpStatus.OK);
    }

    @GetMapping("/admins")
    public ResponseEntity<List<UserBom>> getWatchers(@NotNull @RequestAttribute AccessLevel.LEVEL level, @RequestAttribute Token token, @RequestParam(defaultValue = "true") Boolean needToSort, String search) {
        log.info(String.format(LOG_MESSAGE_TEMPLATE, "getWatchers", LocalDateTime.now(), level, token != null ? token.getUser().getLogin() : "Undefined", "needToSort=" + needToSort + ", search=" + search));
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(userService.findAll(AccessLevel.LEVEL.ADMIN, needToSort, search), HttpStatus.OK);
    }

    @GetMapping("/accessLevels")
    public ResponseEntity<List<AccessLevel>> getAccessLevels(@NotNull @RequestAttribute AccessLevel.LEVEL level, @RequestAttribute Token token) {
        log.info(String.format(LOG_MESSAGE_TEMPLATE, "getAccessLevels", LocalDateTime.now(), level, token != null ? token.getUser().getLogin() : "Undefined", ""));
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(accessLevelService.findAll(), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<UserBom> createUser(@RequestBody UserBom newUser, @NotNull @RequestAttribute AccessLevel.LEVEL level, @RequestAttribute Token token) {
        log.info(String.format(LOG_MESSAGE_TEMPLATE, "createUser", LocalDateTime.now(), level, token != null ? token.getUser().getLogin() : "Undefined", newUser));
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(userService.create(newUser), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<UserBom> updateUser(@RequestBody UserBom updatedUser, @NotNull @RequestAttribute AccessLevel.LEVEL level, @RequestAttribute Token token) {
        log.info(String.format(LOG_MESSAGE_TEMPLATE, "updateUser", LocalDateTime.now(), level, token != null ? token.getUser().getLogin() : "Undefined", updatedUser));
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        return new ResponseEntity<>(userService.update(updatedUser), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id, @NotNull @RequestAttribute AccessLevel.LEVEL level, @RequestAttribute Token token) {
        log.info(String.format(LOG_MESSAGE_TEMPLATE, "deleteUser", LocalDateTime.now(), level, token != null ? token.getUser().getLogin() : "Undefined", id));
        if (!AccessLevel.LEVEL.ADMIN.equals(level)) throw new ForbiddenByAccessLevelException();
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
