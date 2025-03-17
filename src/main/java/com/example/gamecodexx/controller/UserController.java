package com.example.gamecodexx.controller;

import com.example.gamecodexx.entity.User;
import com.example.gamecodexx.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usercontroller")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // Metodo di login personalizzato
    @PostMapping("/login")
    public String login(@RequestParam String nickname,
                        @RequestParam String email,
                        @RequestParam String password,
                        HttpServletRequest request) {
        // Normalizza l'email
        email = email.toLowerCase().trim();
        nickname = nickname.toLowerCase().trim();
        Optional<User> userOptionalNickname = userRepository.findByNickname(nickname);
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptionalNickname.isPresent() && userOptional.isPresent() && passwordEncoder.matches(password, userOptional.get().getPassword())) {
            // Crea il token con un ruolo (o senza, se non vuoi controlli sui ruoli)
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_USER"))
                    );
            // Imposta il token nel SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authToken);

            // Salva manualmente il SecurityContext nella sessione
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            System.out.println("Auth after login = " + SecurityContextHolder.getContext().getAuthentication());

            if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                return "redirect:/homepage";
            } else {
                return "redirect:/auth?error=Autenticazione fallita!";
            }
        } else {
            return "redirect:/auth?error=Credenziali non valide!";
        }
    }


    // Metodo per la registrazione
    @PostMapping("/register")
    public String register(@RequestParam String nickname, @RequestParam String email, @RequestParam String password) {
        // Normalizza l'email
        email = email.toLowerCase().trim();

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
