package academy.devdojo.controller;


import academy.devdojo.mapper.AnimeMapper;
import academy.devdojo.model.Anime;
import academy.devdojo.request.AnimePostRequest;
import academy.devdojo.response.AnimeGetResponse;
import academy.devdojo.response.AnimePostResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("v1/animes")
@Slf4j


public class AnimeController {

    public static final AnimeMapper MAPPER = AnimeMapper.INSTANCE;


    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> list(@RequestParam(required = false) String name) {

        log.debug("Request received to list all animes, filtering by name: '{}'", name);

        List<Anime> animes = Anime.getAnimeList();

        if (name == null)
            return ResponseEntity.ok(MAPPER.toAnimeGetResponseList(animes));

        var animeGetResponseList = MAPPER.toAnimeGetResponseList(
                animes
                        .stream()
                        .filter(anime -> anime.getName().contains(name))
                        .toList());

        return ResponseEntity.ok(animeGetResponseList);

    }


    @GetMapping("/{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find anime by id: {}", id);

        var animeGetResponse = Anime.getAnimeList().stream()
                .filter(anime -> anime.getId().equals(id))
                .map(MAPPER::toAnimeGetResponse)
                .findFirst()
                .orElse(null);

        return ResponseEntity.ok(animeGetResponse);

    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody AnimePostRequest animePostRequest) {

        log.debug("Request to save anime: '{}'", animePostRequest);

        var anime = MAPPER.toAnime(animePostRequest);

        Anime.getAnimeList().add(anime);

        var response = MAPPER.toAnimePostResponse(anime);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
