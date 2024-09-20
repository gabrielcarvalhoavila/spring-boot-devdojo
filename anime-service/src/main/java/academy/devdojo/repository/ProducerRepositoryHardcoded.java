package academy.devdojo.repository;

import academy.devdojo.model.Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProducerRepositoryHardcoded {

    private final ProducerData producers;

    public List<Producer> findAll() {
        return producers.getProducers();
    }

    public Optional<Producer> findById(Long id) {

        return producers.getProducers().stream().filter(producer -> producer.getId().equals(id)).findFirst();
    }

    public List<Producer> findByName(String name) {

        return producers.getProducers().stream().filter(producer -> producer.getName().equalsIgnoreCase(name)).toList();
    }

    public Producer save(Producer producer) {
        producers.getProducers().add(producer);
        return producer;
    }

    public void delete(Producer producer) {
        producers.getProducers().remove(producer);
    }

    public void update(Producer producer) {

        delete(producer);

        save(producer);
    }

}
