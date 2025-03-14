package com.example.gamecodexx.service;

import com.example.gamecodexx.entity.Game;
import com.example.gamecodexx.rawg.RawgGame;
import com.example.gamecodexx.rawg.RawgResponse;
import com.example.gamecodexx.repository.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    // Inserisci qui la tua chiave API RAWG
    private final String apiKey = "b90501a4a11d424aa5c7b53e618d379";
    private final String rawgUrl = "https://api.rawg.io/api/games?key=" + apiKey;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public void fetchAndSaveGames(String searchQuery) {
        String url = rawgUrl + "&search=" + searchQuery;
        RawgResponse response = restTemplate.getForObject(url, RawgResponse.class);

        if (response != null && response.getResults() != null) {
            List<RawgGame> rawgGames = response.getResults();
            rawgGames.stream()
                    .map(this::convertToGame)
                    .forEach(gameRepository::save);
        }
    }

    private Game convertToGame(RawgGame rawgGame) {
        Game game = new Game();
        game.setName(rawgGame.getName());
        game.setReleased(rawgGame.getReleased());
        game.setRating(rawgGame.getRating());
        game.setBackgroundImage(rawgGame.getBackgroundImage());
        // Eventuali conversioni o formattazioni extra possono essere fatte qui
        return game;
    }
}
