package com.gradeBook.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping()
public class CommonController {

    @Value("${app.version:unknown}")
    String version;

    @GetMapping("/version")
    public ResponseEntity<String> getApplicationVersion() {
        return new ResponseEntity<>(version, HttpStatus.OK);
    }

}
