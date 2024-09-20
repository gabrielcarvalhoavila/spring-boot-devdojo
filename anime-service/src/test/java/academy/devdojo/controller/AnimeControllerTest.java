package academy.devdojo.controller;

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
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "academy.devdojo")
class AnimeControllerTest {

    @MockBean
    private AnimeData animeData;
    @SpyBean
    private AnimeRepositoryHardCoded repository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ResourceLoader resourceLoader;
    private List<Anime> animeList;

    @BeforeEach
    void init() {

        var dateString = "2024-09-17T17:30:00.9316657";
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        var localDateTime = LocalDateTime.parse(dateString, formatter);

        var jujutsuKaisen = Anime.builder().id(1L).name("Jujutsu Kaisen").episodes(56L).createdAt(localDateTime).build();
        var towerOfGod = Anime.builder().id(2L).name("Tower of God").episodes(26L).createdAt(localDateTime).build();
        var dragonBallGT = Anime.builder().id(3L).name("Dragon Ball GT").episodes(50L).createdAt(localDateTime).build();
        var bokuNoHero = Anime.builder().id(4L).name("Boku no Hero").episodes(513L).createdAt(localDateTime).build();
        var naruto = Anime.builder().id(5L).name("Naruto Shippuden").episodes(489L).createdAt(localDateTime).build();
        var blackClover = Anime.builder().id(6L).name("Black Clover").episodes(170L).createdAt(localDateTime).build();

        animeList = new ArrayList<>(List.of(jujutsuKaisen, towerOfGod, dragonBallGT, bokuNoHero, naruto, blackClover));

    }

    @Test
    @DisplayName("GET v1/animes returns a list of animes when name is null")
    @Order(1)
    void findAll_ReturnsAllAnimes_WhenNameIsNull() throws Exception {

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var reponse = readResourceFile("/anime/get-anime-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(reponse));

    }

    @Test
    @DisplayName("GET v1/animes?name=Jujutsu Kaisen returns a list of animes when name is found")
    @Order(2)
    void findAll_ReturnsAnimeList_WhenNameIsFound() throws Exception {

        var reponse = readResourceFile("/anime/get-anime-Jujutsu Kaisen-name-200.json");
        var name = "Jujutsu Kaisen";

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);


        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes").param("name", name))
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

        var reponse = readResourceFile("/anime/get-anime-x-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes").param("name", x))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(reponse));

    }

    @Test
    @DisplayName("GET v1/animes/1 returns anime with given Id")
    @Order(4)
    void findByIdOrThrowNotFound_ReturnsAnime_WhenIdIsFound() throws Exception {

        var reponse = readResourceFile("/anime/get-anime-1-by-id-200.json");

        var id = 1L;

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);


        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}", id))
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


        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not found"));
    }

    @Test
    @DisplayName("POST v1/animes saves anime")
    @Order(6)
    void save_ReturnsAnime_WhenSuccessful() throws Exception {

        var animeToBeSaved = Anime.builder().name("Boku no Pico").id(7L).episodes(233L).createdAt(LocalDateTime.now()).build();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(animeToBeSaved);

        var request = readResourceFile("/anime/post-request-anime-200.json");

        var response = readResourceFile("/anime/post-response-anime-201.json");

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/animes").content(request).contentType(MediaType.APPLICATION_JSON))
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

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/animes/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    @DisplayName("DELETE v1/animes/99 throws ResponseStatusException when Anime is not found")
    @Order(8)
    void delete_ThrowsResponseStatusException_WhenAnimeNotFound() throws Exception {

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var id = 99;

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/animes/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not found"));
    }

    @Test
    @DisplayName("PUT v1/animes updates a Anime")
    @Order(9)
    void update_UpdatesAnime_WhenSuccessful() throws Exception {

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var request = readResourceFile("/anime/put-request-anime-200.json");

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/animes").content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }


    @Test
    @DisplayName("PUT v1/animes throws ResponseStatusException 404 when Anime is not found")
    @Order(10)
    void update_ThrowsResponseStatusException_WhenAnimeIsNotFound() throws Exception {

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var request = readResourceFile("/anime/put-request-anime-404.json");

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/animes").content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not found"));

    }


    private String readResourceFile(String filename) throws IOException {
        var file = resourceLoader.getResource("classpath:%s".formatted(filename)).getFile();
        return new String(Files.readAllBytes(file.toPath()));
    }
}