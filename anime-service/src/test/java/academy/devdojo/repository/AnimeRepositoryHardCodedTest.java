package academy.devdojo.repository;

import academy.devdojo.commons.AnimeUtils;
import academy.devdojo.model.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeRepositoryHardCodedTest {

    @InjectMocks
    private AnimeRepositoryHardCoded repository;
    @Mock
    private AnimeData animeData;
    @InjectMocks
    private AnimeUtils animeUtils;
    private List<Anime> animeList;


    @BeforeEach
    void setUp() {
        animeList = animeUtils.getNewAnimes();
    }

    @Test
    @DisplayName("findAll returns a list of animes when successful")
    @Order(1)
    void findAll_ReturnsAllAnimes_WhenSuccessful() {

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        List<Anime> animes = repository.findAll();

        Assertions.assertThat(animes).isNotEmpty().isNotNull().hasSameElementsAs(animeList);
    }

    @Test
    @DisplayName("findById returns a Anime when successful")
    @Order(2)
    void findById_ReturnsAnime_WhenSuccessful() {

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var expectedAnime = animeList.getFirst();

        var optionalAnime = repository.findById(expectedAnime.getId());

        Assertions.assertThat(optionalAnime).isPresent();
        Assertions.assertThat(optionalAnime.get().getId()).isEqualTo(expectedAnime.getId());
    }

    @Test
    @DisplayName("findByName returns a List of Anime when name is found")
    @Order(3)
    void findByName_ReturnsListOfAnime_WhenNameIsFound() {

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var expectedAnime = animeList.getFirst();

        var animeListByName = repository.findByName(expectedAnime.getName());

        Assertions.assertThat(animeListByName).isNotNull().isNotEmpty();
        Assertions.assertThat(animeListByName.getFirst()).isEqualTo(expectedAnime).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("findByName returns a empty list when name is not found")
    @Order(4)
    void findByName_ReturnsEmptyList_WhenNameIsNotFound() {

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        List<Anime> animeListByName = repository.findByName("NOT FOUND");

        Assertions.assertThat(animeListByName).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByName returns a empty list when name is null")
    @Order(5)
    void findByName_ReturnsEmptyList_WhenNameIsNull() {

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        List<Anime> animeListByName = repository.findByName(null);

        Assertions.assertThat(animeListByName).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("save returns a Anime when successful")
    @Order(6)
    void save_ReturnsAnime_WhenSuccessful() {

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var anime = animeUtils.getNewAnime();

        var savedAnime = repository.save(anime);

        Assertions.assertThat(savedAnime).isNotNull().isEqualTo(anime).hasNoNullFieldsOrProperties();

        Assertions.assertThat(animeList).isNotEmpty().contains(savedAnime);

    }

    @Test
    @DisplayName("delete removes a Anime when successful")
    @Order(7)
    void delete_RemovesAnime_WhenSuccessful() {

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var removedAnime = animeList.getFirst();

        repository.delete(removedAnime);

        Assertions.assertThat(animeList).isNotNull().isNotEmpty().doesNotContain(removedAnime);

    }

    @Test
    @DisplayName("update updates a Anime when successful")
    @Order(8)
    void update_UpdatesAnime_WhenSuccessful() {

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var animeToBeUpdated = animeList.getFirst();

        animeToBeUpdated.setName("Spy Family");
        animeToBeUpdated.setEpisodes(24L);

        repository.update(animeToBeUpdated);

        Assertions.assertThat(animeList).isNotNull().isNotEmpty().contains(animeToBeUpdated);

    }


}