package academy.devdojo.controller;


import academy.devdojo.commons.FileUtils;
import academy.devdojo.model.User;
import academy.devdojo.repository.UserData;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ComponentScan(value = "academy.devdojo")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    private static final String URL = "/v1/users";
    private static final String RESOURCES_DIRECTORY = "src/test/resources";

    @MockBean
    private UserData userData;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileUtils fileUtils;

    private final List<User> userList = new ArrayList<>();

    @BeforeEach
    void setup() {
        var gabriel = new User(1L, "Gabriel", "Carvalho", "gabriel.carvalho@gmail.com");
        var dave = new User(2L, "Jane", "Smith", "Dave.Winchester@hotmail.com");
        var emily = new User(3L, "Emily", "Clark", "emily.clark@outlook.com");
        var nelson = new User(4L, "Nelson", "Avila", "nelson.avila@terra.com.br");
        var chris = new User(5L, "Chris", "Evans", "chris.evans@example.com");
        var otakao = new User(6L, "Leonardo", "Dutra", "leonardo.dutra@playvitta.com");

        userList.addAll(List.of(gabriel, dave, nelson, chris, emily, otakao));
    }


    @Test
    @DisplayName("GET /v1/users returns list of all Users when param name is not present")
    @Order(1)
    void findAll_ReturnsAllUsersWhenParamNameIsNotPresent() throws Exception {

        BDDMockito.when(userData.getUsers()).thenReturn(userList);


        var response = fileUtils.readFromFile(RESOURCES_DIRECTORY + "/response/get-user-null-firstName-200.json");


        mockMvc.perform(get(URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    @DisplayName("GET /v1/users?firstName=Gabriel returns list of Users that param:firstName matches")
    @Order(2)
    void findAll_ReturnsListOfUsers_ThatParamFirstNameMatches() throws Exception {

        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var response = fileUtils.readFromFile(RESOURCES_DIRECTORY + "/response/get-user-Gabriel-firstName-200.json");

        mockMvc.perform(get(URL).param("firstName", "Gabriel"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    @DisplayName("GET /v1/users?firstName=NOTFOUND returns list of Users that param:firstName matches")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenFirstNameIsNotFound() throws Exception {

        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var response = fileUtils.readFromFile(RESOURCES_DIRECTORY + "/response/get-user-NOTFOUND-firstName-200.json");

        mockMvc.perform(get(URL).param("firstName", "NOTFOUND"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    @DisplayName("GET /v1/users/1 returns found User")
    @Order(4)
    void findById_ReturnsFoundUser() throws Exception {

        var response = fileUtils.readFromFile(RESOURCES_DIRECTORY + "/response/get-user-1-by-id-200.json");

        Long id = 1L;

        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        mockMvc.perform(get(URL + "/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    @DisplayName("GET /v1/users/9999 throws ResponseStatusException when Id is not found")
    @Order(5)
    void findById_ThrowsResponseStatusException404_WhenIdIsNotFound() throws Exception {

        var id = 9999L;

        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        mockMvc.perform(get(URL + "/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(status().reason("User not found"));
    }

    @Test
    @DisplayName("POST /v1/users returns user when successful")
    @Order(6)
    void save_ReturnsUser_WhenSuccessful() throws Exception {

        var request = fileUtils.readFromFile(RESOURCES_DIRECTORY + "/request/post-request-200.json");
        var response = fileUtils.readFromFile(RESOURCES_DIRECTORY + "/response/post-response-201.json");

        mockMvc.perform(post(URL).content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(response));
    }

    @Test
    @DisplayName("DELETE /v1/users/1 deletes User")
    @Order(7)
    void delete_DeletesUser() throws Exception {

        var id = 1L;

        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        mockMvc.perform(delete(URL + "/{id}", id))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /v1/users/9999 throws ResponseStatusException when id is not found")
    @Order(8)
    void delete_ThrowsResponseStatusException404_WhenIdIsNotFound() throws Exception {

        var id = 9999L;

        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        mockMvc.perform(delete(URL + "/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(status().reason("User not found"));
    }

    @Test
    @DisplayName("PUT /v1/users updates User")
    @Order(9)
    void update_updatesUser() throws Exception {

        var request = fileUtils.readFromFile(RESOURCES_DIRECTORY + "/request/put-request-200.json");

        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        mockMvc.perform(put(URL).content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("PUT /v1/users throws ResponseStatusException when id is not found")
    @Order(10)
    void update_ThrowsResponseStatusException404_WhenIdIsNotFound() throws Exception {

        var request = fileUtils.readFromFile(RESOURCES_DIRECTORY + "/request/put-request-404.json");

        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        mockMvc.perform(put(URL).content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


}