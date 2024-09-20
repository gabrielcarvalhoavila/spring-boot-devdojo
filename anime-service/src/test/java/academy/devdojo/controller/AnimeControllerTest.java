package academy.devdojo.controller;

import academy.devdojo.commons.AnimeUtils;
import academy.devdojo.commons.FileUtils;
import academy.devdojo.model.Anime;
import academy.devdojo.repository.AnimeData;
import academy.devdojo.repository.AnimeRepositoryHardCoded;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "academy.devdojo")
class AnimeControllerTest {

    private static final String URL = "/v1/animes";

    @MockBean
    private AnimeData animeData;
    @SpyBean
    private AnimeRepositoryHardCoded repository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private AnimeUtils animeUtils;
    private List<Anime> animeList;

    @BeforeEach
    void init() {
        animeList = animeUtils.getNewAnimes();
    }

    @Test
    @DisplayName("GET v1/animes returns a list of animes when name is null")
    @Order(1)
    void findAll_ReturnsAllAnimes_WhenNameIsNull() throws Exception {

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var reponse = fileUtils.readResourcerFile("/anime/get-anime-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(reponse));

    }

    @Test
    @DisplayName("GET v1/animes?name=Jujutsu Kaisen returns a list of animes when name is found")
    @Order(2)
    void findAll_ReturnsAnimeList_WhenNameIsFound() throws Exception {

        var reponse = fileUtils.readResourcerFile("/anime/get-anime-Jujutsu Kaisen-name-200.json");
        var name = "Jujutsu Kaisen";

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);


        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(reponse));


    }


    @Test
    @DisplayName("GET v1/animes?name=x returns an empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() throws Exception {

        var x = "NOT_FOUND";

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var reponse = fileUtils.readResourcerFile("/anime/get-anime-x-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", x))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(reponse));

    }

    @Test
    @DisplayName("GET v1/animes/1 returns anime with given Id")
    @Order(4)
    void findByIdOrThrowNotFound_ReturnsAnime_WhenIdIsFound() throws Exception {

        var reponse = fileUtils.readResourcerFile("/anime/get-anime-1-by-id-200.json");

        var id = 1L;

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);


        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(reponse));

    }

    @Test
    @DisplayName("GET v1/animes/99 throws ResponseStatusException 404 when id is not found")
    @Order(5)
    void findByIdOrThrowNotFound_ThrowsResponseStatusException404_WhenIdNotFound() throws Exception {

        var id = 99L;

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);


        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not found"));
    }

    @Test
    @DisplayName("POST v1/animes saves anime")
    @Order(6)
    void save_ReturnsAnime_WhenSuccessful() throws Exception {

        var animeToBeSaved = animeUtils.getNewAnime();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(animeToBeSaved);

        var request = fileUtils.readResourcerFile("/anime/post-request-anime-200.json");

        var response = fileUtils.readResourcerFile("/anime/post-response-anime-201.json");

        mockMvc.perform(MockMvcRequestBuilders.post(URL).content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("DELETE v1/animes/1 removes a Anime")
    @Order(7)
    void delete_RemovesAnime_WhenSuccessful() throws Exception {

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var animeToBeRemoved = animeList.getFirst();

        var id = animeToBeRemoved.getId();

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    @DisplayName("DELETE v1/animes/99 throws ResponseStatusException when Anime is not found")
    @Order(8)
    void delete_ThrowsResponseStatusException_WhenAnimeNotFound() throws Exception {

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var id = 99;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not found"));
    }

    @Test
    @DisplayName("PUT v1/animes updates a Anime")
    @Order(9)
    void update_UpdatesAnime_WhenSuccessful() throws Exception {

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var request = fileUtils.readResourcerFile("/anime/put-request-anime-200.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL).content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }


    @Test
    @DisplayName("PUT v1/animes throws ResponseStatusException 404 when Anime is not found")
    @Order(10)
    void update_ThrowsResponseStatusException_WhenAnimeIsNotFound() throws Exception {

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var request = fileUtils.readResourcerFile("/anime/put-request-anime-404.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL).content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not found"));

    }
}