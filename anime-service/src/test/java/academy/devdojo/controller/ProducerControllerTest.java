package academy.devdojo.controller;


import academy.devdojo.commons.FileUtils;
import academy.devdojo.commons.ProducerUtils;
import academy.devdojo.model.Producer;
import academy.devdojo.repository.ProducerData;
import academy.devdojo.repository.ProducerRepositoryHardcoded;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(controllers = ProducerController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@Import({ProducerMapperImpl.class, ProducerService.class, ProducerRepositoryHardcoded.class, ProducerData.class})
@ComponentScan(basePackages = "academy.devdojo")
//@ActiveProfiles("test")
class ProducerControllerTest {

    private static final String URL = "/v1/producers";

    @Autowired
    private MockMvc mockMvc;

    private List<Producer> producerList;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private ProducerUtils producerUtils;

    @MockBean
    private ProducerData producerData;

    @SpyBean
    private ProducerRepositoryHardcoded repository;

    @BeforeEach
    void init() {
        producerList = producerUtils.getNewProducers();
    }


    @Test
    @DisplayName("GET v1/producers returns a list with all producers when argument is null")
    @Order(1)
    void findAll_ReturnsAllProducers_WhenArgumentIsNull() throws Exception {

        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var response = fileUtils.readResourcerFile("producer/get-producer-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers?name=Ufotable returns a list with found object when name exists")
    @Order(2)
    void findAll_ReturnsListWithFoundObject_WhenNameExists() throws Exception {


        var response = fileUtils.readResourcerFile("producer/get-producer-ufotable-name-200.json");
        var name = "Ufotable";


        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);


        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("GET v1/producers?name=x returns an empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() throws Exception {

        var response = fileUtils.readResourcerFile("producer/get-producer-x-name-200.json");
        var name = "x";
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);


        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @Test
    @DisplayName("GET v1/producers/1 returns Producer with given Id")
    @Order(4)
    void findById_ReturnsProducer_WhenSuccessful() throws Exception {

        var response = fileUtils.readResourcerFile("producer/get-producer-by-id-1-200.json");

        var id = 1L;

        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
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

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not found"));

    }


    @Test
    @DisplayName("POST v1/producers creates a producer")
    @Order(6)
    void save_ReturnsProducer_WhenSuccessful() throws Exception {

        var producerToSave = producerUtils.getNewProducer();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(producerToSave);


        var request = fileUtils.readResourcerFile("producer/post-request-producer-200.json");
        var response = fileUtils.readResourcerFile("producer/post-response-producer-201.json");

        mockMvc.perform(MockMvcRequestBuilders.post(URL).content(request).contentType(MediaType.APPLICATION_JSON))
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


        var request = fileUtils.readResourcerFile("producer/put-request-producer-200.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL).content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }


    @Test
    @DisplayName("PUT v1/producers throws ResponseStatusException when producer not found")
    @Order(8)
    void update_ThrowsResponseStatusException_WhenProducerNotFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var request = fileUtils.readResourcerFile("producer/put-request-producer-404.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL).content(request).contentType(MediaType.APPLICATION_JSON))
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
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());


    }

    @Test
    @DisplayName("DELETE v1/producers/99 throws ResponseStatusException when producer not found")
    @Order(10)
    void delete_ThrowsResponseStatusException_WhenIdNotFound() throws Exception {

        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not found"));
    }

    @ParameterizedTest
    @MethodSource(value = "postArgumentsSource")
    @DisplayName("POST v1/producers throws Bad Request when fields are invalid")
    @Order(11)
    void save_ThrowsBadRequest_WhenFieldsAreInvalid(String filename) throws Exception {

        var invalidNameError = "Attribute 'name' is required";

        var request = fileUtils.readResourcerFile(filename);

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/v1/producers").contentType(MediaType.APPLICATION_JSON).content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        Assertions.assertThat(mvcResult.getResolvedException()).isNotNull();

        var errorMessage = mvcResult.getResolvedException().getMessage();

        Assertions.assertThat(errorMessage).contains(invalidNameError);
    }

    @ParameterizedTest
    @MethodSource(value = "postArgumentsSource")
    @DisplayName("PUT v1/producers throws Bad Request when fields are invalid")
    @Order(11)
    void update_ThrowsBadRequest_WhenFieldsAreInvalid(String filename) throws Exception {

        var invalidNameError = "Attribute 'name' is required";
        var invalidIdError = "Attribute 'id' is required";

        var request = fileUtils.readResourcerFile(filename);

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/v1/producers").contentType(MediaType.APPLICATION_JSON).content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        Assertions.assertThat(mvcResult.getResolvedException()).isNotNull();

        var errorMessage = mvcResult.getResolvedException().getMessage();

        Assertions.assertThat(errorMessage).contains(invalidNameError, invalidIdError);
    }

    private static Stream<Arguments> postArgumentsSource(){
        return Stream.of(
                Arguments.of("/producer/post-request-producer-blank-fields-400.json"),
                Arguments.of("/producer/post-request-producer-empty-fields-400.json"),
                Arguments.of("/producer/post-request-producer-null-fields-400.json")
        );
    }

    private static Stream<Arguments> putArgumentsSource(){
        return Stream.of(
                Arguments.of("/producer/put-request-producer-blank-fields-400.json"),
                Arguments.of("/producer/put-request-producer-empty-fields-400.json"),
                Arguments.of("/producer/put-request-producer-null-fields-400.json")
        );
    }


}