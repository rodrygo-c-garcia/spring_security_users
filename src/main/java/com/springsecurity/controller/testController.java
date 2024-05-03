package com.springsecurity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")

// web securuty
@PreAuthorize("denyAll()")
public class testController {

    @GetMapping("/greeting")
    @PreAuthorize("permitAll()")
    public String test() {
        return "Hello World";
    }

    @GetMapping("/data")
    @PreAuthorize("hasAuthority('write')")
    public String data() {
        return "data 1234";
    }
}