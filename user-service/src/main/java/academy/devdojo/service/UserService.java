package academy.devdojo.service;

import academy.devdojo.exception.NotFoundException;
import academy.devdojo.model.User;
import academy.devdojo.repository.UserRepositoryHardcoded;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepositoryHardcoded repository;


    public List<User> findAll(String name) {
        if (name == null) return repository.findAll();
        return repository.findByFirstName(name);
    }

    public User findByIdOrThrowException(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User save(User user) {
        return repository.save(user);
    }

    public void delete(Long id) {

        var user = this.findByIdOrThrowException(id);

        repository.delete(user);

    }

    public void update(User user) {

        this.findByIdOrThrowException(user.getId());

        repository.update(user);
    }
}
