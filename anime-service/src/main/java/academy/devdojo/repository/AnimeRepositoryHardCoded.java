package academy.devdojo.repository;

import academy.devdojo.model.Anime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnimeRepositoryHardCoded {
    private static List<Anime> ANIMES = new ArrayList<>();

    static {
        ANIMES.add(Anime.builder().id(1L).name("Naruto").episodes(220L).createdAt(LocalDateTime.now()).build());
        ANIMES.add(Anime.builder().id(2L).name("Bleach").episodes(366L).createdAt(LocalDateTime.now()).build());
        ANIMES.add(Anime.builder().id(3L).name("One Piece").episodes(1000L).createdAt(LocalDateTime.now()).build());
        ANIMES.add(Anime.builder().id(4L).name("Dragon Ball Z").episodes(291L).createdAt(LocalDateTime.now()).build());
        ANIMES.add(Anime.builder().id(5L).name("Boku no Hero").episodes(88L).createdAt(LocalDateTime.now()).build());
        ANIMES.add(Anime.builder().id(6L).name("Black Clover").episodes(170L).createdAt(LocalDateTime.now()).build());
    }

    public List<Anime> findAll() {
        return ANIMES;
    }

    public Optional<Anime> findById(Long id) {
        return ANIMES.stream().filter(anime -> anime.getId().equals(id)).findFirst();
    }

    public List<Anime> findByName(String name) {
        return ANIMES.stream().filter(anime -> anime.getName().contains(name)).toList();
    }

    public Anime save(Anime anime) {
        ANIMES.add(anime);
        return anime;
    }

    public void delete(Anime anime) {
        ANIMES.remove(anime);
    }

    public void update(Anime anime) {

        delete(anime);

        save(anime);
    }


}
