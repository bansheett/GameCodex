package com.example.gamecodexx.service;

import com.example.gamecodexx.entity.Game;
import com.example.gamecodexx.rawg.RawgGame;
import com.example.gamecodexx.rawg.RawgResponse;
import com.example.gamecodexx.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class GameService {
    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    private final RestTemplate restTemplate;
    private final GameRepository gameRepository;
    private final String RAWG_API_KEY = "b90501a4a11d424aa5c7b53e618d379e";
    private final String RAWG_URL = "https://api.rawg.io/api/games?key=" + RAWG_API_KEY;

    // Configurazioni che potresti voler spostare in application.properties
    @Value("${rawg.page.size:20}")
    private int pageSize;

    @Value("${rawg.request.delay:3000}")
    private int requestDelay;

    @Value("${rawg.max.pages:500}")
    private int maxPages;

    // Contatore per tenere traccia del progresso
    private final AtomicInteger processedGames = new AtomicInteger(0);

    public GameService(RestTemplate restTemplate, GameRepository gameRepository) {
        this.restTemplate = restTemplate;
        this.gameRepository = gameRepository;
    }

    // Metodo per cercare e salvare giochi in base a un termine di ricerca
    public void fetchAndSaveGames(String search) {
        String url = RAWG_URL + "&search=" + search + "&page_size=" + pageSize;
        logger.info("Searching for: {}", search);

        try {
            RawgResponse response = restTemplate.getForObject(url, RawgResponse.class);

            if (response != null && response.getResults() != null && !response.getResults().isEmpty()) {
                List<RawgGame> rawgGames = response.getResults();

                // Converti e salva i giochi
                rawgGames.stream()
                        .map(this::convertToGame)
                        .forEach(gameRepository::save);

                logger.info("Saved {} games for search term: {}", rawgGames.size(), search);
            } else {
                logger.info("No games found for search term: {}", search);
            }
        } catch (Exception e) {
            logger.error("Error fetching games for search term: {}", search, e);
        }
    }

    // Metodo asincrono per recuperare tutti i giochi
    @Async
    public void fetchAndSaveAllGames() {
        int page = 1;
        boolean hasMoreGames = true;
        processedGames.set(0);

        logger.info("Starting to fetch all games from RAWG API");

        while (hasMoreGames && page <= maxPages) {
            String url = RAWG_URL + "&page=" + page + "&page_size=" + pageSize;
            logger.info("Fetching page: {} (processed games so far: {})", page, processedGames.get());

            try {
                RawgResponse response = restTemplate.getForObject(url, RawgResponse.class);

                if (response != null && response.getResults() != null && !response.getResults().isEmpty()) {
                    List<RawgGame> rawgGames = response.getResults();

                    // Batch processing per migliorare le prestazioni
                    rawgGames.stream()
                            .map(this::convertToGame)
                            .forEach(game -> {
                                try {
                                    gameRepository.save(game);
                                    processedGames.incrementAndGet();
                                } catch (Exception e) {
                                    logger.error("Error saving game: {}", game.getName(), e);
                                }
                            });

                    page++; // Passa alla pagina successiva
                } else {
                    hasMoreGames = false; // Se la risposta Ã¨ vuota, finisce il ciclo
                    logger.info("No more games to fetch or empty response");
                }

                // Pausa per rispettare i limiti di rate dell'API
                Thread.sleep(requestDelay);

            } catch (HttpClientErrorException e) {
                logger.error("API error: {}", e.getMessage());
                if (e.getRawStatusCode() == 429) {
                    // Too Many Requests - aumenta il tempo di attesa
                    logger.info("Rate limit exceeded, waiting for 30 seconds");
                    try {
                        Thread.sleep(30000); // Attendi 30 secondi
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    hasMoreGames = false;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                hasMoreGames = false;
                logger.error("Thread interrupted", e);
            } catch (Exception e) {
                logger.error("Unexpected error fetching page {}", page, e);
                // Continua con la prossima pagina invece di interrompere tutto il processo
                page++;
            }
        }

        logger.info("Finished fetching games. Total games processed: {}", processedGames.get());
    }

    // Metodo per ottenere lo stato attuale del processo di recupero
    public String getFetchStatus() {
        return "Processed games: " + processedGames.get();
    }

    private Game convertToGame(RawgGame rawgGame) {
        Game game = new Game();
        game.setName(rawgGame.getName());
        game.setReleased(rawgGame.getReleased());
        game.setRating(rawgGame.getRating());
        game.setBackgroundImage(rawgGame.getBackgroundImage());

        // Aggiungi qui altri campi se necessario

        return game;
    }
}