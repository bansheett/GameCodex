package com.example.gamecodexx.controller;

import com.example.gamecodexx.entity.User;
import com.example.gamecodexx.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/usercontroller")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // Metodo per la registrazione
    @PostMapping("/register")
    public String register(@RequestParam String nickname, @RequestParam String email, @RequestParam String password) {
        // Normalizza l'email
        email = email.toLowerCase().trim();
        System.out.println("ciaoooo prova");

        // Controlla il dominio dell'email
        if (!email.matches(".*@(gmail\\.com|yahoo\\.com|libero\\.it)$")) {
            return "redirect:/auth?error=Email non valida!";
        }
        // Controlla se l'email esiste già
        if (userRepository.findByEmail(email).isPresent()) {
            return "redirect:/auth?error=Email già in uso!";
        }

        // Crea un nuovo utente
        User user = new User();
        user.setNickname(nickname);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setAttempts(0);

        // Salva l'utente nel database
        userRepository.save(user);

        return "redirect:/auth?success=Registrazione avvenuta con successo!";
    }



}
