package academy.devdojo.controller;


import academy.devdojo.model.Anime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static academy.devdojo.model.Anime.getAnimeList;

@RestController
@RequestMapping("v1/animes")
@Slf4j


public class AnimeController {


    @GetMapping
    public List<Anime> list() {
        return getAnimeList();
    }

    @GetMapping("list2")
    public List<Anime> filterByName(@RequestParam(defaultValue = "") String name) {
        return getAnimeList().stream().filter(anime -> anime.getName().contains(name)).toList();
    }

    @GetMapping("/{id}")
    public List<Anime> list3(@PathVariable Long id) {
        return getAnimeList().stream().filter(anime -> anime.getId().equals(id)).toList();
    }


}
