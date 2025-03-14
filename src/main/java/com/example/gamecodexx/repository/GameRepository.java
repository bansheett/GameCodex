package com.example.gamecodexx.repository;

import com.example.gamecodexx.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
    // Puoi aggiungere query personalizzate se necessario
}
