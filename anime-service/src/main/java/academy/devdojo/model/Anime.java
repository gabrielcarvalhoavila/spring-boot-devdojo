package academy.devdojo.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class Anime {
    private Long id;
    private String name;
    private Long episodes;
    @Getter
    private static List<Anime> animeList = new ArrayList<>();
    private LocalDateTime createdAt;

    static {
        animeList.add(Anime.builder().id(1L).name("Naruto").episodes(220L).createdAt(LocalDateTime.now()).build());
        animeList.add(Anime.builder().id(2L).name("Bleach").episodes(366L).createdAt(LocalDateTime.now()).build());
        animeList.add(Anime.builder().id(3L).name("One Piece").episodes(1000L).createdAt(LocalDateTime.now()).build());
        animeList.add(Anime.builder().id(4L).name("Dragon Ball Z").episodes(291L).createdAt(LocalDateTime.now()).build());
        animeList.add(Anime.builder().id(5L).name("Boku no Hero").episodes(88L).createdAt(LocalDateTime.now()).build());
        animeList.add(Anime.builder().id(6L).name("Black Clover").episodes(170L).createdAt(LocalDateTime.now()).build());
    }

}
