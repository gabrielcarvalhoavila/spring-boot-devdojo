package academy.devdojo.controller;


import academy.devdojo.model.Producer;
import academy.devdojo.repository.ProducerData;
import academy.devdojo.repository.ProducerRepositoryHardcoded;
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

@WebMvcTest(controllers = ProducerController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@Import({ProducerMapperImpl.class, ProducerService.class, ProducerRepositoryHardcoded.class, ProducerData.class})
@ComponentScan(basePackages = "academy.devdojo")
class ProducerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private List<Producer> producerList;

    @MockBean
    private ProducerData producerData;

    @SpyBean
    private ProducerRepositoryHardcoded repository;


    @Autowired
    private ResourceLoader resourceLoader;

    @BeforeEach
    void init() {

        var dateTime = "2024-09-17T09:23:25.125372";
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        var localDateTime = LocalDateTime.parse(dateTime, formatter);

        var ufotable = Producer.builder().id(1L).name("Ufotable").createdAt(localDateTime).build();
        var kyotoAnimation = Producer.builder().id(2L).name("Kyoto Animation").createdAt(localDateTime).build();
        var a1Pictures = Producer.builder().id(3L).name("A-1 Pictures").createdAt(localDateTime).build();
        var witStudio = Producer.builder().id(4L).name("Wit Studio").createdAt(localDateTime).build();
        var trigger = Producer.builder().id(5L).name("Trigger").createdAt(localDateTime).build();

        producerList = new ArrayList<>(List.of(ufotable, kyotoAnimation, a1Pictures, witStudio, trigger));
    }


    @Test
    @DisplayName("GET v1/producers returns a list with all producers when argument is null")
    @Order(1)
    void findAll_ReturnsAllProducers_WhenArgumentIsNull() throws Exception {

        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var response = readResourcerFile("producer/get-producer-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers?name=Ufotable returns a list with found object when name exists")
    @Order(2)
    void findAll_ReturnsListWithFoundObject_WhenNameExists() throws Exception {


        var response = readResourcerFile("producer/get-producer-ufotable-name-200.json");
        var name = "Ufotable";


        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);


        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("GET v1/producers?name=x returns an empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() throws Exception {

        var response = readResourcerFile("producer/get-producer-x-name-200.json");
        var name = "x";
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);


        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("GET v1/producers/1 returns Producer with given Id")
    @Order(4)
    void findById_ReturnsProducer_WhenSuccessful() throws Exception {

        var response = readResourcerFile("producer/get-producer-by-id-1-200.json");

        var id = 1L;

        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET v1/producers/99 throws ResponseStatusException 404 when Producer not found")
    @Order(5)
    void findById_ThrowsResponseStatusException_WhenProducerNotFound() throws Exception {

        var id = 99L;

        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not found"));

    }


    @Test
    @DisplayName("POST v1/producers creates a producer")
    @Order(6)
    void save_ReturnsProducer_WhenSuccessful() throws Exception {

        var producerToSave = Producer.builder().id(6L).name("Aniplex").createdAt(LocalDateTime.now()).build();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(producerToSave);


        var request = readResourcerFile("producer/post-request-producer-200.json");
        var response = readResourcerFile("producer/post-response-producer-201.json");

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/producers").content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));


    }


    @Test
    @DisplayName("PUT v1/producers updates a producer")
    @Order(7)
    void update_UpdatesProducer_WhenSuccessful() throws Exception {

        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);


        var request = readResourcerFile("producer/put-request-producer-200.json");

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/producers").content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }


    @Test
    @DisplayName("PUT v1/producers throws ResponseStatusException when producer not found")
    @Order(8)
    void update_ThrowsResponseStatusException_WhenProducerNotFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var request = readResourcerFile("producer/put-request-producer-404.json");

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/producers").content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not found"));

    }


    @Test
    @DisplayName("DELETE v1/producers/1 removes a producer when successful")
    @Order(9)
    void delete_RemovesProducer_WhenSuccessful() throws Exception {

        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var id = producerList.getFirst().getId();
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());


    }

    @Test
    @DisplayName("DELETE v1/producers/99 throws ResponseStatusException when producer not found")
    @Order(10)
    void delete_ThrowsResponseStatusException_WhenIdNotFound() throws Exception {

        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not found"));
    }


    private String readResourcerFile(String filename) throws IOException {
        var file = resourceLoader.getResource("classpath:%s".formatted(filename)).getFile();
        return new String(Files.readAllBytes(file.toPath()));
    }

}