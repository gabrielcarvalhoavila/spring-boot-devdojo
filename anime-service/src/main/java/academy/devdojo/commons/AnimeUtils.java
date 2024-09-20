package academy.devdojo.commons;

import academy.devdojo.model.Anime;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeUtils {

    public List<Anime> getNewAnimes() {
        var dateString = "2024-09-17T17:30:00.9316657";
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        var localDateTime = LocalDateTime.parse(dateString, formatter);

        var jujutsuKaisen = Anime.builder().id(1L).name("Jujutsu Kaisen").episodes(56L).createdAt(localDateTime).build();
        var towerOfGod = Anime.builder().id(2L).name("Tower of God").episodes(26L).createdAt(localDateTime).build();
        var dragonBallGT = Anime.builder().id(3L).name("Dragon Ball GT").episodes(50L).createdAt(localDateTime).build();
        var bokuNoHero = Anime.builder().id(4L).name("Boku no Hero").episodes(513L).createdAt(localDateTime).build();
        var naruto = Anime.builder().id(5L).name("Naruto Shippuden").episodes(489L).createdAt(localDateTime).build();
        var blackClover = Anime.builder().id(6L).name("Black Clover").episodes(170L).createdAt(localDateTime).build();

        return new ArrayList<>(List.of(jujutsuKaisen, towerOfGod, dragonBallGT, bokuNoHero, naruto, blackClover));
    }

    public Anime getNewAnime() {
        return Anime.builder().name("Boku no Pico").id(7L).episodes(233L).createdAt(LocalDateTime.now()).build();
    }
}
