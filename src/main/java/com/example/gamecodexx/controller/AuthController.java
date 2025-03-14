package com.example.gamecodexx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/auth")
    public String showLoginPage() {
        return "auth"; // mostrerà auth.html
    }



}
