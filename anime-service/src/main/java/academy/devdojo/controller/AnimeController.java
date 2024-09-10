package academy.devdojo.controller;


import academy.devdojo.mapper.AnimeMapper;
import academy.devdojo.request.AnimePostRequest;
import academy.devdojo.request.AnimePutRequest;
import academy.devdojo.response.AnimeGetResponse;
import academy.devdojo.response.AnimePostResponse;
import academy.devdojo.service.AnimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("v1/animes")
@Slf4j


public class AnimeController {

    private AnimeService animeService;

    public static final AnimeMapper MAPPER = AnimeMapper.INSTANCE;

    public AnimeController() {
        this.animeService = new AnimeService();
    }


    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> list(@RequestParam(required = false) String name) {

        log.debug("Request received to list all animes, filtering by name: '{}'", name);

        var animes = animeService.findAll(name);

        var responseList = MAPPER.toAnimeGetResponseList(animes);

        return ResponseEntity.ok(responseList);
    }


    @GetMapping("/{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find anime by id: {}", id);

        var anime = animeService.findByIdOrThrowNotFound(id);

        var animeGetResponse = MAPPER.toAnimeGetResponse(anime);

        return ResponseEntity.ok(animeGetResponse);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody AnimePostRequest animePostRequest) {

        log.debug("Request to save anime: '{}'", animePostRequest);

        var anime = animeService.save(MAPPER.toAnime(animePostRequest));

        var response = MAPPER.toAnimePostResponse(anime);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.debug("Deleting anime with id {}", id);

        animeService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody AnimePutRequest animePutRequest) {
        log.debug("Request to update anime: '{}'", animePutRequest);

        var anime = MAPPER.toAnime(animePutRequest);

        animeService.update(anime);

        return ResponseEntity.noContent().build();
    }


}
