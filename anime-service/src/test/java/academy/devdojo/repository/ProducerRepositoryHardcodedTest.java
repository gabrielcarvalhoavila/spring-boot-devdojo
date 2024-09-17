package academy.devdojo.repository;

import academy.devdojo.model.Producer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerRepositoryHardcodedTest {

    @InjectMocks
    private ProducerRepositoryHardcoded repository;
    @Mock
    private ProducerData producerData;

    private List<Producer> producerList;

    @BeforeEach
    void init() {
        var ufotable = Producer.builder().id(1L).name("Ufotable").build();
        var kyotoAnimation = Producer.builder().id(2L).name("Kyoto Animation").build();
        var a1Pictures = Producer.builder().id(3L).name("A-1 Pictures").build();
        var witStudio = Producer.builder().id(4L).name("Wit Studio").build();
        var trigger = Producer.builder().id(5L).name("Trigger").build();

        producerList = new ArrayList<>(List.of(ufotable, kyotoAnimation, a1Pictures, witStudio, trigger));
    }

    @Test
    @DisplayName("findAll returns a list of producers when successful")
    @Order(1)
    void findAll_ReturnsAllProducers_WhenSuccessful() {

        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producers = repository.findAll();

        Assertions.assertThat(producers)
                .isNotNull()
                .isNotEmpty()
                .hasSameElementsAs(producerList);
    }

    @Test
    @DisplayName("findById returns a producer when successful")
    @Order(2)
    void findById_ReturnsProducer_WhenSuccessful() {

        var expectedProducer = producerList.getFirst();

        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producerFound = repository.findById(expectedProducer.getId());

        Assertions.assertThat(producerFound)
                .isNotNull()
                .isPresent()
                .contains(expectedProducer);
    }

    @Test
    @DisplayName("findByName returns a list of producer when name is found")
    @Order(3)
    void findByName_ReturnsFoundProducerInList_WhenNameIsFound() {

        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producer = producerList.getFirst();

        var producers = repository.findByName(producer.getName());

        Assertions.assertThat(producers)
                .isNotNull()
                .isNotEmpty()
                .contains(producer);

    }

    @Test
    @DisplayName("findByName returns an empty list when name is not found")
    @Order(4)
    void findByName_ReturnsEmptyList_WhenNameIsNotFound() {

        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producers = repository.findByName("Not Found");

        Assertions.assertThat(producers)
                .isNotNull()
                .isEmpty();

    }

    @Test
    @DisplayName("findByName returns empty list when name is null")
    @Order(5)
    void findByName_ReturnsEmptyList_WhenNameIsNull() {

        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producers = repository.findByName(null);

        Assertions.assertThat(producers)
                .isNotNull()
                .isEmpty();

    }

    @Test
    @DisplayName("save creates a producer when successful")
    @Order(6)
    void save_ReturnsProducer_WhenSuccessful() {

        var producer = Producer.builder().id(6L).name("Sunrise").createdAt(LocalDateTime.now()).build();

        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var savedProducer = repository.save(producer);

        Assertions.assertThat(savedProducer)
                .isNotNull()
                .isEqualTo(producer)
                .hasNoNullFieldsOrProperties();

        Assertions.assertThat(producerList)
                .isNotEmpty()
                .contains(savedProducer);

    }

    @Test
    @DisplayName("delete removes a producer when successful")
    @Order(7)
    void delete_RemovesProducer_WhenSuccessful() {

        var producer = producerList.getFirst();

        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        repository.delete(producer);

        var producers = repository.findAll();

        Assertions.assertThat(producers)
                .isNotNull()
                .isNotEmpty()
                .doesNotContain(producer);

    }

    @Test
    @DisplayName("update updates a producer when successful")
    @Order(8)
    void update_UpdatesProducer_WhenSuccessful() {

        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producerToUpdate = producerList.getFirst();

        producerToUpdate.setName("Sunrise");

        repository.update(producerToUpdate);

        var producers = repository.findAll();

        Assertions.assertThat(producers)
                .isNotNull()
                .isNotEmpty()
                .contains(producerToUpdate);

        Optional<Producer> optionalProducer = repository.findById(producerToUpdate.getId());

        Assertions.assertThat(optionalProducer).isPresent();

        Assertions.assertThat(optionalProducer.get().getName()).isEqualTo(producerToUpdate.getName());

    }


}