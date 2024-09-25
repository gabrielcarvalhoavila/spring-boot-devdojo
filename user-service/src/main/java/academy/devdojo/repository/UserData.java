package academy.devdojo.repository;

import academy.devdojo.model.User;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class UserData {

    private List<User> users = new ArrayList<>();


    {

        var john = new User(1L, "John", "Doe", "john.doe@example.com");
        var jane = new User(2L, "Jane", "Smith", "jane.smith@example.com");
        var alex = new User(3L, "Alex", "Johnson", "alex.johnson@example.com");
        var chris = new User(4L, "Chris", "Evans", "chris.evans@example.com");
        var emily = new User(5L, "Emily", "Clark", "emily.clark@example.com");

        users.addAll(List.of(john, jane, alex, chris, emily));
    }

}
