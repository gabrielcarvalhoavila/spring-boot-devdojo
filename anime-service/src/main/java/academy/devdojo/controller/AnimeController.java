package academy.devdojo.controller;


import academy.devdojo.mapper.AnimeMapper;
import academy.devdojo.request.anime.AnimePostRequest;
import academy.devdojo.request.anime.AnimePutRequest;
import academy.devdojo.response.anime.AnimeGetResponse;
import academy.devdojo.response.anime.AnimePostResponse;
import academy.devdojo.service.AnimeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("v1/animes")
@Slf4j
@RequiredArgsConstructor

public class AnimeController {

    private final AnimeMapper mapper;
    private final AnimeService animeService;

    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> list(@RequestParam(required = false) String name) {

        log.debug("Request received to list all animes, filtering by name: '{}'", name);

        var animes = animeService.findAll(name);

        var responseList = mapper.toAnimeGetResponseList(animes);

        return ResponseEntity.ok(responseList);
    }


    @GetMapping("/{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find anime by id: {}", id);

        var anime = animeService.findByIdOrThrowNotFound(id);

        var animeGetResponse = mapper.toAnimeGetResponse(anime);

        return ResponseEntity.ok(animeGetResponse);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@Valid @RequestBody AnimePostRequest animePostRequest) {

        log.debug("Request to save anime: '{}'", animePostRequest);

        var anime = animeService.save(mapper.toAnime(animePostRequest));

        var response = mapper.toAnimePostResponse(anime);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<Void> update(@Valid @RequestBody AnimePutRequest animePutRequest) {
        log.debug("Request to update anime: '{}'", animePutRequest);

        var anime = mapper.toAnime(animePutRequest);

        animeService.update(anime);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@Valid @Min(1) @PathVariable Long id) {
        log.debug("Deleting anime with id {}", id);

        animeService.delete(id);

        return ResponseEntity.noContent().build();
    }


}
