package com.gradeBook.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin
@Controller
@RequiredArgsConstructor
@RequestMapping
public class CommonController {

    @Value("${spring.app.version:unknown}")
    String version;

    @GetMapping
    public String getApplicationVersion() {
        return version;
    }

}
