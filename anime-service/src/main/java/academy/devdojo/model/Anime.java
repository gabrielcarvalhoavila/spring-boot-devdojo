package academy.devdojo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Anime {
    private Long id;
    private String name;
    private Long episodes;
    @Getter
    private static List<Anime> animeList = new ArrayList<>();

    static {
        animeList.add(new Anime(1L, "Naruto", 500L));
        animeList.add(new Anime(2L, "Boku no hero", 133L));
        animeList.add(new Anime(3L, "One piece", 1000L));
        animeList.add(new Anime(4L, "Dragon ball", 200L));
        animeList.add(new Anime(5L, "Naruto Shipudden", 360L));
    }

}
