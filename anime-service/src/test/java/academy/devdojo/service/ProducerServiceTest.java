package academy.devdojo.service;

import academy.devdojo.model.Producer;
import academy.devdojo.repository.ProducerRepositoryHardcoded;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerServiceTest {

    @InjectMocks
    private ProducerService service;

    @Mock
    private ProducerRepositoryHardcoded repository;

    private List<Producer> producerList;

    @BeforeEach
    void setup() {
        var ufotable = Producer.builder().id(1L).name("Ufotable").createdAt(LocalDateTime.now()).build();
        var kyotoAnimation = Producer.builder().id(2L).name("Kyoto Animation").createdAt(LocalDateTime.now()).build();
        var a1Pictures = Producer.builder().id(3L).name("A-1 Pictures").createdAt(LocalDateTime.now()).build();
        var witStudio = Producer.builder().id(4L).name("Wit Studio").createdAt(LocalDateTime.now()).build();
        var trigger = Producer.builder().id(5L).name("Trigger").createdAt(LocalDateTime.now()).build();

        producerList = new ArrayList<>(List.of(ufotable, kyotoAnimation, a1Pictures, witStudio, trigger));
    }


    @Test
    @DisplayName("findAll returns all producers when argument is null")
    @Order(1)
    void findAll_ReturnsAllProducers_WhenArgumentIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(producerList);

        var producers = service.findAll(null);

        Assertions.assertThat(producers).isNotNull().isNotEmpty().hasSameElementsAs(producerList);

    }

    @Test
    @DisplayName("findAll returns Producer in List when argument matches")
    @Order(2)
    void findAll_ReturnsFoundAnimeInList_WhenArgumentMatches() {
        var producer = producerList.getFirst();

        BDDMockito.when(repository.findByName(producer.getName())).thenReturn(List.of(producerList.getFirst()));


        var producers = service.findAll(producer.getName());

        Assertions.assertThat(producers).isNotNull().isNotEmpty();

        Assertions.assertThat(List.of(producerList.getFirst())).contains(producer);

    }

    @Test
    @DisplayName("findAll returns empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() {

        var name = "NOT FOUND";

        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.emptyList());

        var producers = service.findAll(name);

        Assertions.assertThat(producers).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById returns Producer with given Id")
    @Order(4)
    void findById_ReturnsProducer_WhenSuccessful() {

        var producerToBeFound = producerList.getFirst();

        BDDMockito.when(repository.findById(producerToBeFound.getId())).thenReturn(Optional.of(producerList.getFirst()));

        var producerFound = service.findByIdOrThrowNotFound(producerToBeFound.getId());

        Assertions.assertThat(producerFound).isNotNull().isEqualTo(producerToBeFound);
    }

    @Test
    @DisplayName("findById throws ResponseStatusException when Producer not found")
    @Order(5)
    void findById_ThrowsResponseStatusException_WhenProducerNotFound() {


        BDDMockito.when(repository.findById(99L)).thenReturn(Optional.empty());


        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(99L))
                .isInstanceOf(ResponseStatusException.class);

    }

    @Test
    @DisplayName("save creates a producer when successful")
    @Order(6)
    void save_ReturnsProducer_WhenSuccessful() {

        var producerToSave = Producer.builder().id(6L).name("Sunrise").createdAt(LocalDateTime.now()).build();

        BDDMockito.when(repository.save(producerToSave)).thenReturn(producerToSave);

        var savedProducer = service.save(producerToSave);

        Assertions.assertThat(savedProducer)
                .isNotNull()
                .isEqualTo(producerToSave)
                .hasNoNullFieldsOrProperties();

        Mockito.verify(repository).save(producerToSave);
    }

    @Test
    @DisplayName("delete removes a producer when successful")
    @Order(7)
    void delete_RemovesProducer_WhenSuccessful() {

        var producerToBeRemoved = producerList.getFirst();

        BDDMockito.when(repository.findById(producerToBeRemoved.getId())).thenReturn(Optional.of(producerToBeRemoved));

        BDDMockito.doNothing().when(repository).delete(producerToBeRemoved);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(producerToBeRemoved.getId()));

    }

    @Test
    @DisplayName("delete throws ResponseStatusException when producer not found")
    @Order(8)
    void delete_ThrowsResponseStatusException_WhenIdNotFound() {

        var producerToBeRemoved = 99L;

        BDDMockito.when(repository.findById(producerToBeRemoved)).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.delete(producerToBeRemoved)).isInstanceOf(ResponseStatusException.class);

    }

    @Test
    @DisplayName("update updates a producer when successful")
    @Order(9)
    void update_UpdatesProducer_WhenSuccessful() {

        var producerToUpdate = producerList.getFirst();

        producerToUpdate.setName("Sunrise");

        BDDMockito.when(repository.findById(producerToUpdate.getId())).thenReturn(Optional.of(producerToUpdate));

        BDDMockito.doNothing().when(repository).update(producerToUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(producerToUpdate));

    }


    @Test
    @DisplayName("update throws ResponseStatusException when producer not found")
    @Order(10)
    void update_ThrowsResponseStatusException_WhenProducerNotFound() {

        var producerToUpdate = producerList.getFirst();

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.update(producerToUpdate)).isInstanceOf(ResponseStatusException.class);

    }


}