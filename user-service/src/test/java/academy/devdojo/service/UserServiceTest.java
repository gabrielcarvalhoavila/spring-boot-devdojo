package academy.devdojo.service;

import academy.devdojo.commons.UserUtils;
import academy.devdojo.model.User;
import academy.devdojo.repository.UserRepositoryHardcoded;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;
    @Mock
    private UserRepositoryHardcoded repository;
    @InjectMocks
    private UserUtils userUtils;

    private List<User> userList;

    @BeforeEach
    void setup() {
        userList = userUtils.createNewUserList();
    }

    @Test
    @DisplayName("findAll Returns list of all Users when name is null")
    @Order(1)
    void findAll_ReturnsListOfUsersWhenSuccessful() {
        BDDMockito.when(repository.findAll()).thenReturn(userList);

        var users = service.findAll(null);

        Assertions.assertThat(users)
                .isNotNull()
                .isNotEmpty()
                .hasSameElementsAs(userList);
    }

    @Test
    @DisplayName("findAll Returns user list with found users when name is found")
    @Order(2)
    void findAll_ReturnsUserListWithFoundUser_WhenNameIsFound() {

        var user = userList.getFirst();

        BDDMockito.when(repository.findByFirstName(user.getFirstName())).thenReturn(List.of(userList.getFirst()));

        var users = service.findAll(user.getFirstName());

        Assertions.assertThat(users)
                .isNotNull()
                .isNotEmpty()
                .hasSameElementsAs(List.of(userList.getFirst()));
    }

    @Test
    @DisplayName("findAll Returns empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() {

        var userNotFound = "NOT FOUND";

        BDDMockito.when(repository.findByFirstName(userNotFound)).thenReturn(Collections.emptyList());

        var users = service.findAll(userNotFound);

        Assertions.assertThat(users)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("findById returns found User when successful")
    @Order(4)
    void findById_ReturnsFoundUser_WhenSuccessful() {

        var expectedUser = userList.getFirst();

        BDDMockito.when(repository.findById(expectedUser.getId())).thenReturn(Optional.of(userList.getFirst()));

        var foundUser = service.findByIdOrThrowException(expectedUser.getId());

        Assertions.assertThat(foundUser)
                .isNotNull()
                .isEqualTo(expectedUser);
    }

    @Test
    @DisplayName("findById throws ResponseStatusException when Id is not Found")
    @Order(5)
    void findById_ThrowsReponseStatusException_WhenIdIsNotFound() {

        var id = 99L;
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.findByIdOrThrowException(id));
    }

    @Test
    @DisplayName("save returns user when successful")
    @Order(6)
    void save_ReturnsUser_WhenSuccessful() {

        var userToBeSaved = userUtils.createNewUser();

        BDDMockito.when(repository.save(userToBeSaved)).thenReturn(userToBeSaved);

        var savedUser = service.save(userToBeSaved);

        Assertions.assertThat(savedUser)
                .isNotNull()
                .isEqualTo(userToBeSaved)
                .hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("delete removes user when successful")
    @Order(7)
    void delete_RemovesUser_WhenSuccessful() {

        var userToBeDeleted = userList.getFirst();

        BDDMockito.when(repository.findById(userToBeDeleted.getId())).thenReturn(Optional.of(userToBeDeleted));

        BDDMockito.doNothing().when(repository).delete(userToBeDeleted);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(userToBeDeleted.getId()));
    }

    @Test
    @DisplayName("delete throws exception when id is not found")
    @Order(8)
    void delete_ThrowsReponseStatusException_WhenIdIsNotFound() {

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.delete(ArgumentMatchers.anyLong())).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update updates user when successful")
    @Order(9)
    void update_UpdatesUser_WhenSuccessful() {

        var userToBeUpdated = userList.getFirst();

        BDDMockito.doNothing().when(repository).update(userToBeUpdated);

        BDDMockito.when(repository.findById(userToBeUpdated.getId())).thenReturn(Optional.of(userList.getFirst()));

        userToBeUpdated.setFirstName("Goku");
        userToBeUpdated.setLastName("Marroá");
        userToBeUpdated.setEmail("goku.marroa@dbz.com");

        service.update(userToBeUpdated);

        Assertions.assertThat(userList).contains(userToBeUpdated);

    }


    @Test
    @DisplayName("update throws ResponseStatusException when User is not found")
    @Order(10)
    void update_ThrowsResponseStatusException_WhenUserIsNotFound() {

        var animeToBeUpdated = userList.getFirst();

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());


        Assertions.assertThatException().isThrownBy(() -> service.update(animeToBeUpdated)).isInstanceOf(ResponseStatusException.class);

    }



}