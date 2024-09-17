package academy.devdojo.repository;

import academy.devdojo.model.Anime;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class AnimeData {

    private final List<Anime> animes = new ArrayList<>();

    {
        animes.add(Anime.builder().id(1L).name("Naruto").episodes(220L).createdAt(LocalDateTime.now()).build());
        animes.add(Anime.builder().id(2L).name("Bleach").episodes(366L).createdAt(LocalDateTime.now()).build());
        animes.add(Anime.builder().id(3L).name("One Piece").episodes(1000L).createdAt(LocalDateTime.now()).build());
        animes.add(Anime.builder().id(4L).name("Dragon Ball Z").episodes(291L).createdAt(LocalDateTime.now()).build());
        animes.add(Anime.builder().id(5L).name("Boku no Hero").episodes(88L).createdAt(LocalDateTime.now()).build());
        animes.add(Anime.builder().id(6L).name("Black Clover").episodes(170L).createdAt(LocalDateTime.now()).build());
    }


}
