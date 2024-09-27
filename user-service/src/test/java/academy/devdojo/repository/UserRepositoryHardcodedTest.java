package academy.devdojo.repository;

import academy.devdojo.commons.UserUtils;
import academy.devdojo.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryHardcodedTest {
    @InjectMocks
    private UserRepositoryHardcoded repository;

    @Mock
    private UserData userData;

    @InjectMocks
    private UserUtils userUtils;

    private List<User> userList;


    @BeforeEach
    void setup() {
        userList = userUtils.createNewUserList();
    }


    @Test
    @DisplayName("findAll Returns list of all Users when successful")
    @Order(1)
    void findAll_ReturnsListOfUsersWhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var usersRepository = repository.findAll();

        Assertions.assertThat(usersRepository)
                .isNotNull()
                .isNotEmpty()
                .hasSameElementsAs(userList);
    }

    @Test
    @DisplayName("findById returns found User when successful")
    @Order(2)
    void findById_ReturnsFoundUser_WhenSuccessful() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userToBeFound = Optional.of(userList.getFirst());

        var foundUser = repository.findById(userToBeFound.get().getId());

        Assertions.assertThat(foundUser)
                .isPresent()
                .isNotNull()
                .isEqualTo(userToBeFound);
    }

    @Test
    @DisplayName("findById returns empty when User is not found")
    @Order(3)
    void findById_ReturnsEmpty_WhenNotFound() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userToBeNotFound = 99L;

        var foundUser = repository.findById(userToBeNotFound);


        Assertions.assertThat(foundUser)
                .isNotNull()
                .isNotPresent();
    }

    @Test
    @DisplayName("findByFirstName returns list of Users when name is found")
    @Order(4)
    void findByName_ReturnsListOfUsers_WhenNameIsFound() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userToBeFound = userList.getFirst().getFirstName();

        var foundUser = repository.findByFirstName(userToBeFound);

        Assertions.assertThat(foundUser)
                .isNotNull()
                .isNotEmpty()
                .hasSameElementsAs(List.of(userList.getFirst()));

    }

    @Test
    @DisplayName("findByFirstName returns empty list when name is not found")
    @Order(5)
    void findByName_ReturnsEmptyList_WhenNameIsNotFound() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userToBeNotFound = "NOT FOUND";

        var foundUser = repository.findByFirstName(userToBeNotFound);

        Assertions.assertThat(foundUser)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("findByFirstName returns empty list when name is not found")
    @Order(6)
    void findByName_ReturnsEmptyList_WhenNameIsNull() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);


        var foundUser = repository.findByFirstName(null);

        Assertions.assertThat(foundUser)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save returns user when successful")
    @Order(7)
    void save_ReturnsUser_WhenSuccessful() {

        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userToBeSaved = userUtils.createNewUser();

        var savedUser = repository.save(userToBeSaved);

        Assertions.assertThat(savedUser)
                .isNotNull()
                .isEqualTo(userToBeSaved)
                .hasNoNullFieldsOrProperties();

        Assertions.assertThat(userList).contains(savedUser);
    }

    @Test
    @DisplayName("delete removes user when successful")
    @Order(8)
    void delete_RemovesUser_WhenSuccessful() {

        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userToBeDeleted = userList.getFirst();

        repository.delete(userToBeDeleted);

        Assertions.assertThat(userList).doesNotContain(userToBeDeleted);
    }

    @Test
    @DisplayName("update updates user when successful")
    @Order(9)
    void update_UpdatesUser_WhenSuccessful() {

        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userToBeUpdated = userList.getFirst();

        userToBeUpdated.setFirstName("Goku");
        userToBeUpdated.setLastName("Marroá");
        userToBeUpdated.setEmail("goku.marroa@dbz.com");

        repository.update(userToBeUpdated);

        Assertions.assertThat(userList).contains(userToBeUpdated);

    }


}