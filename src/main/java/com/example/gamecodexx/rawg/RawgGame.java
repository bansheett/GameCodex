package com.example.gamecodexx.rawg;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class RawgGame {
    private String name;
    private String released;
    private Double rating;

    @JsonProperty("background_image")
    private String backgroundImage;

    // Se necessario, gestisci qui la lista delle piattaforme
    private List<Object> platforms;
}
