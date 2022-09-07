package com.gradeBook.controller;

import com.gradeBook.entity.Pupil;
import com.gradeBook.entity.Teacher;
import com.gradeBook.entity.User;
import com.gradeBook.entity.Watcher;
import com.gradeBook.repository.PupilRepo;
import com.gradeBook.repository.TeacherRepo;
import com.gradeBook.repository.UserRepo;
import com.gradeBook.repository.WatcherRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {

    private final UserRepo userRepo;
    private final PupilRepo pupilRepo;
    private final WatcherRepo watcherRepo;
    private final TeacherRepo teacherRepo;

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userRepo.findAll(), HttpStatus.OK);
    }

    @GetMapping("pupils")
    public ResponseEntity<List<Pupil>> getPupils() {
        return new ResponseEntity<>(pupilRepo.findAll(), HttpStatus.OK);
    }

    @GetMapping("teachers")
    public ResponseEntity<List<Teacher>> getTeachers() {
        return new ResponseEntity<>(teacherRepo.findAll(), HttpStatus.OK);
    }

    @GetMapping("watchers")
    public ResponseEntity<List<Watcher>> getWatchers() {
        return new ResponseEntity<>(watcherRepo.findAll(), HttpStatus.OK);
    }

    @PostMapping("teachers")
    public ResponseEntity<User> createTeacher(@RequestBody Teacher teacher) {
        return new ResponseEntity<>(userRepo.saveAndFlush(teacher), HttpStatus.OK);
    }

    @PostMapping("watchers")
    public ResponseEntity<User> createWatcher(@RequestBody Watcher watcher) {
        return new ResponseEntity<>(userRepo.saveAndFlush(watcher), HttpStatus.OK);
    }

    @PostMapping("pupils")
    public ResponseEntity<User> createPupil(@RequestBody Pupil pupil) {
        return new ResponseEntity<>(userRepo.saveAndFlush(pupil), HttpStatus.OK);
    }

    @PutMapping("teachers")
    public ResponseEntity<User> updateTeacher(@RequestBody Teacher teacher) {
        return new ResponseEntity<>(userRepo.saveAndFlush(teacher), HttpStatus.OK);
    }

    @PutMapping("watchers")
    public ResponseEntity<User> updateWatcher(@RequestBody Watcher watcher) {
        return new ResponseEntity<>(userRepo.saveAndFlush(watcher), HttpStatus.OK);
    }

    @PutMapping("pupils")
    public ResponseEntity<User> updatePupil(@RequestBody Pupil pupil) {
        return new ResponseEntity<>(userRepo.saveAndFlush(pupil), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> updateTeacher(@PathVariable Long id) {
        userRepo.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
