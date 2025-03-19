package com.example.gamecodexx.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "game")
@Data
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String released;
    private Double rating;

    @Column(name = "background_image")
    private String backgroundImage;

    // Aggiungi altri campi se necessario
}
