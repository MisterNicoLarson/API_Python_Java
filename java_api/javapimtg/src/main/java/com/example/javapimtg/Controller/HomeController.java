package com.example.javapimtg.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

/**
 * HomeController is a REST controller for handling the home page of the application.
 *
 * <p>This controller handles the following operations:</p>
 * <ul>
 *     <li>GET requests to the root URL ("/") to display a welcome message and the User-Agent of the requesting client.</li>
 * </ul>
 */
@RestController
@RequestMapping("/")
public class HomeController {

    /**
     * Handles GET requests to the root URL ("/").
     *
     * <p>This method retrieves the User-Agent header from the request and returns a welcome message
     * along with the User-Agent information.</p>
     *
     * @param request The WebRequest object representing the HTTP request.
     * @return A welcome message including the User-Agent information.
     */
    @GetMapping
    public String home(WebRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return "Welcome to the JavAPI MTG!\nYour User-Agent is: " + userAgent;
    }
}
