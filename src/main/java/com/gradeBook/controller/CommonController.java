package com.gradeBook.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping()
@Slf4j
public class CommonController {

    @Value("${app.version:unknown}")
    String version;

    private static final String LOG_MESSAGE_TEMPLATE = "[CommonController.%s] [%s] [Response: %s]";

    @GetMapping("/version")
    public ResponseEntity<String> getApplicationVersion() {
        log.info(String.format(LOG_MESSAGE_TEMPLATE, "getApplicationVersion", LocalDateTime.now(), version));
        return new ResponseEntity<>(version, HttpStatus.OK);
    }

}
