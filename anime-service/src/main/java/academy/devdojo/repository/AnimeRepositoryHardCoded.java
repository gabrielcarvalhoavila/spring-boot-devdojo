package academy.devdojo.repository;

import academy.devdojo.model.Anime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AnimeRepositoryHardCoded {

    private final AnimeData animes;

    public List<Anime> findAll() {
        return animes.getAnimes();
    }

    public Optional<Anime> findById(Long id) {
        return animes.getAnimes().stream().filter(anime -> anime.getId().equals(id)).findFirst();
    }

    public List<Anime> findByName(String name) {
        return animes.getAnimes().stream().filter(anime -> anime.getName().equalsIgnoreCase(name)).toList();
    }

    public Anime save(Anime anime) {
        animes.getAnimes().add(anime);
        return anime;
    }

    public void delete(Anime anime) {
        animes.getAnimes().remove(anime);
    }

    public void update(Anime anime) {

        delete(anime);

        save(anime);
    }


}
