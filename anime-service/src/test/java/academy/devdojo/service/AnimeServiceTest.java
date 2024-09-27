package academy.devdojo.service;


import academy.devdojo.commons.AnimeUtils;
import academy.devdojo.model.Anime;
import academy.devdojo.repository.AnimeRepositoryHardCoded;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService service;
    @Mock
    private AnimeRepositoryHardCoded repository;
    @InjectMocks
    private AnimeUtils animeUtils;

    private List<Anime> animeList;

    @BeforeEach
    void init() {
        animeList = animeUtils.getNewAnimes();
    }

    @Test
    @DisplayName("findAll returns a list of animes when name is null")
    @Order(1)
    void findAll_ReturnsAllAnimes_WhenNameIsNull() {

        BDDMockito.when(repository.findAll()).thenReturn(animeList);

        var animes = service.findAll(null);

        Assertions.assertThat(animes).isNotEmpty().isNotNull().hasSameElementsAs(animeList);
    }

    @Test
    @DisplayName("findAll returns a list of animes when name is found")
    @Order(2)
    void findAll_ReturnsAnimeList_WhenNameIsFound() {

        var anime = animeList.getFirst();

        BDDMockito.when(repository.findByName(anime.getName())).thenReturn(List.of(animeList.getFirst()));

        var animesFound = service.findAll(anime.getName());

        Assertions.assertThat(animesFound).isNotEmpty().isNotNull().hasSameElementsAs(List.of(animeList.getFirst()));
    }

    @Test
    @DisplayName("findAll returns an empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() {

        var name = "NOT_FOUND";

        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.emptyList());

        var animesFound = service.findAll(name);

        Assertions.assertThat(animesFound).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByIdOrThrowNotFound returns anime when id is found")
    @Order(4)
    void findByIdOrThrowNotFound_ReturnsAnime_WhenIdIsFound() {

        var animeToBeFound = animeList.getFirst();

        BDDMockito.when(repository.findById(animeToBeFound.getId())).thenReturn(Optional.of(animeToBeFound));

        var animeFound = service.findByIdOrThrowNotFound(animeToBeFound.getId());

        Assertions.assertThat(animeFound).isNotNull().isEqualTo(animeToBeFound);

    }

    @Test
    @DisplayName("findByIdOrThrowNotFound throws ResponseStatusException when anime not found")
    @Order(5)
    void findByIdOrThrowNotFound_ThrowsResponseStatusException_WhenAnimeNotFound() {

        var id = 99L;

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.findByIdOrThrowNotFound(id)).isInstanceOf(ResponseStatusException.class);

    }

    @Test
    @DisplayName("save returns a Anime when successful")
    @Order(6)
    void save_ReturnsAnime_WhenSuccessful() {

        var animeToBeSaved = animeUtils.getNewAnime();

        BDDMockito.when(repository.save(animeToBeSaved)).thenReturn(animeToBeSaved);

        var animeSaved = service.save(animeToBeSaved);

        Assertions.assertThat(animeSaved).isNotNull().isEqualTo(animeToBeSaved).hasNoNullFieldsOrProperties();

    }

    @Test
    @DisplayName("delete removes a Anime when successful")
    @Order(7)
    void delete_RemovesAnime_WhenSuccessful() {

        var animeToBeRemoved = animeList.getFirst();

        BDDMockito.when(repository.findById(animeToBeRemoved.getId())).thenReturn(Optional.of(animeToBeRemoved));

        BDDMockito.doNothing().when(repository).delete(animeToBeRemoved);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(animeToBeRemoved.getId()));

    }

    @Test
    @DisplayName("delete throws ResponseStatusException when Anime is not found")
    @Order(8)
    void delete_ThrowsResponseStatusException_WhenAnimeNotFound() {

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());


        Assertions.assertThatException().isThrownBy(() -> service.delete(ArgumentMatchers.anyLong())).isInstanceOf(ResponseStatusException.class);

    }

    @Test
    @DisplayName("update updates a Anime when successful")
    @Order(9)
    void update_UpdatesAnime_WhenSuccessful() {

        var animeToBeUpdated = animeList.getFirst();

        animeToBeUpdated.setName("Spy Family");

        animeToBeUpdated.setEpisodes(24L);

        BDDMockito.when(repository.findById(animeToBeUpdated.getId())).thenReturn(Optional.of(animeToBeUpdated));

        BDDMockito.doNothing().when(repository).update(animeToBeUpdated);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(animeToBeUpdated));

    }

    @Test
    @DisplayName("update throws ResponseStatusException when Anime is not found")
    @Order(10)
    void update_ThrowsResponseStatusException_WhenAnimeIsNotFound() {

        var animeToBeUpdated = animeList.getFirst();

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());


        Assertions.assertThatException().isThrownBy(() -> service.update(animeToBeUpdated)).isInstanceOf(ResponseStatusException.class);

    }
}