package academy.devdojo.repository;

import academy.devdojo.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryHardcoded {

    private final UserData userData;

    public List<User> findAll() {

        return userData.getUsers();

    }

    public List<User> findByFirstName(String name){

        return userData.getUsers().stream().filter(u -> u.getFirstName().equalsIgnoreCase(name)).toList();

    }

    public Optional<User> findById(Long id){

        return userData.getUsers().stream().filter(u -> u.getId().equals(id)).findFirst();

    }

    public User save(User user){

        userData.getUsers().add(user);

        return user;
    }

    public void delete(User user){

        userData.getUsers().remove(user);
    }

    public void update(User user){

        this.delete(user);

        this.save(user);

    }




}
