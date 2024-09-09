package academy.devdojo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Anime {
    private Long id;
    private String name;
    private Long episodes;


    public static List<Anime> getAnimeList() {
        return new ArrayList<>(List.of(
                new Anime(1L, "Naruto", 500L),
                new Anime(2L, "Boku no hero", 133L),
                new Anime(3L, "One piece", 1000L),
                new Anime(4L, "Dragon ball", 200L),
                new Anime(5L, "Naruto Shipudden", 360L)));

    }
}
