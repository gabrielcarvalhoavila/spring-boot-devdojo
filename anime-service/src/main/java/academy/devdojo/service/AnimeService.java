package academy.devdojo.service;
import academy.devdojo.model.Anime;
import academy.devdojo.repository.AnimeRepositoryHardCoded;

import java.util.List;

public class AnimeService {

    private AnimeRepositoryHardCoded animeRepository;

    public AnimeService() {
        this.animeRepository = new AnimeRepositoryHardCoded();
    }

    public List<Anime> findAll(String name) {

        if (name == null) return animeRepository.findAll();

        return animeRepository.findByName(name);
    }

    public Anime findByIdOrThrowNotFound(long id) {
        return animeRepository.findById(id).orElseThrow(() -> new RuntimeException("Anime not found"));
    }

    public Anime save(Anime anime) {
        return animeRepository.save(anime);
    }

    public void delete(Long id) {

        Anime anime = findByIdOrThrowNotFound(id);

        animeRepository.delete(anime);
    }

    public void update(Anime animeToUpdate) {

        var anime = findByIdOrThrowNotFound(animeToUpdate.getId());

        animeToUpdate.setCreatedAt(anime.getCreatedAt());

        animeRepository.update(animeToUpdate);

    }


}
