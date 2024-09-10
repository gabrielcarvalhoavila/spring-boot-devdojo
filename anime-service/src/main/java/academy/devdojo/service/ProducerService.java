package academy.devdojo.service;

import academy.devdojo.model.Producer;
import academy.devdojo.repository.ProducerRepositoryHardcoded;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class ProducerService {
    private ProducerRepositoryHardcoded producerRepository;

    public ProducerService() {
        this.producerRepository = new ProducerRepositoryHardcoded();
    }

    public List<Producer> findAll(String name) {
        if (name == null) {
            return producerRepository.findAll();
        }
        return producerRepository.findByName(name);
    }

    public Producer findByIdOrThrowNotFound(long id) {
        return producerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producer not found"));
    }

    public Producer save(Producer producer) {
        return producerRepository.save(producer);
    }

    public void delete(Long id) {
        Producer producer = findByIdOrThrowNotFound(id);
        producerRepository.delete(producer);
    }

    public void update(Producer producerToUpdate) {
        var producer = findByIdOrThrowNotFound(producerToUpdate.getId());
        producerToUpdate.setCreatedAt(producer.getCreatedAt());
        producerRepository.update(producerToUpdate);
    }
}
