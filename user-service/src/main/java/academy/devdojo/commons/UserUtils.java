package academy.devdojo.commons;

import academy.devdojo.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserUtils {

    public List<User> createNewUserList(){

        var gabriel = new User(1L, "Gabriel", "Carvalho", "gabriel.carvalho@gmail.com");
        var dave = new User(2L, "Jane", "Smith", "Dave.Winchester@hotmail.com");
        var emily = new User(3L, "Emily", "Clark", "emily.clark@outlook.com");
        var nelson = new User(4L, "Nelson", "Avila", "nelson.avila@terra.com.br");
        var chris = new User(5L, "Chris", "Evans", "chris.evans@example.com");
        var otakao = new User(6L, "Leonardo", "Dutra", "leonardo.dutra@playvitta.com");

        return new ArrayList<>(List.of(gabriel, dave, nelson, chris, emily, otakao));
    }

    public User createNewUser(){
        return new User(7L, "Cleo", "Pum", "cleo-pum@globo.com");
    }
}
