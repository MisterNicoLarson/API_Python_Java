package com.example.javapimtg.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String home(WebRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return "Welcome to the JavAPI MTG!\nYour User-Agent is: " + userAgent;
    }
}
