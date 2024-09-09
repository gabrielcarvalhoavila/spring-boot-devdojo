package academy.devdojo.controller;


import academy.devdojo.model.Anime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@RestController
@RequestMapping("v1/animes")
@Slf4j


public class AnimeController {


    @GetMapping
    public List<Anime> list() {
        return Anime.getAnimeList();
    }

    @GetMapping("list2")
    public List<Anime> filterByName(@RequestParam(defaultValue = "") String name) {
        return Anime.getAnimeList().stream().filter(anime -> anime.getName().contains(name)).toList();
    }

    @GetMapping("/{id}")
    public List<Anime> list3(@PathVariable Long id) {
        return Anime.getAnimeList().stream().filter(anime -> anime.getId().equals(id)).toList();
    }

    @PostMapping
    public Anime save(@RequestBody Anime anime) {
        Long id = ThreadLocalRandom.current().nextLong(1, 1000);
        anime.setId(id);
        Anime.getAnimeList().add(anime);
        return anime;
    }


}
