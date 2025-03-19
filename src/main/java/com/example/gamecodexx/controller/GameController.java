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

    // Endpoint per avviare il recupero di tutti i giochi
    @PostMapping("/fetch-all")
    public ResponseEntity<String> fetchAllGames() {
        gameService.fetchAndSaveAllGames();
        return ResponseEntity.ok("Avviato il processo di recupero di tutti i giochi. Questo potrebbe richiedere molto tempo.");
    }

    // Endpoint per controllare lo stato del processo di recupero
    @GetMapping("/fetch-status")
    public ResponseEntity<String> getFetchStatus() {
        return ResponseEntity.ok(gameService.getFetchStatus());
    }
}
