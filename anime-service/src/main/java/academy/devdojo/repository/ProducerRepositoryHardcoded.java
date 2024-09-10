package academy.devdojo.repository;

import academy.devdojo.model.Producer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProducerRepositoryHardcoded {
    private static final List<Producer> PRODUCERS = new ArrayList<>();


    static {
        PRODUCERS.add(Producer.builder().id(1L).name("Studio Ghibli").createdAt(LocalDateTime.now()).build());
        PRODUCERS.add(Producer.builder().id(2L).name("Madhouse").createdAt(LocalDateTime.now()).build());
        PRODUCERS.add(Producer.builder().id(3L).name("Bones").createdAt(LocalDateTime.now()).build());
        PRODUCERS.add(Producer.builder().id(4L).name("Toei Animation").createdAt(LocalDateTime.now()).build());
        PRODUCERS.add(Producer.builder().id(5L).name("Sunrise").createdAt(LocalDateTime.now()).build());
    }

    public List<Producer> findAll() {
        return PRODUCERS;
    }

    public Optional<Producer> findById(long id) {
        return PRODUCERS.stream().filter(producer -> producer.getId().equals(id)).findFirst();
    }

    public List<Producer> findByName(String name) {
        return PRODUCERS.stream().filter(producer -> producer.getName().contains(name)).toList();
    }

    public Producer save(Producer producer) {
        PRODUCERS.add(producer);
        return producer;
    }

    public void delete(Producer producer) {
        PRODUCERS.remove(producer);
    }

    public void update(Producer producer) {
        delete(producer);
        save(producer);
    }

}
