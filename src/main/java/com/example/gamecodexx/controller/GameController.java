package com.example.gamecodexx.controller;

import com.example.gamecodexx.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // Endpoint per effettuare la ricerca e salvare i giochi nel database
    @PostMapping("/fetch")
    public ResponseEntity<String> fetchGames(@RequestParam String search) {
        gameService.fetchAndSaveGames(search);
        return ResponseEntity.ok("Giochi recuperati e salvati nel database!");
    }
}
