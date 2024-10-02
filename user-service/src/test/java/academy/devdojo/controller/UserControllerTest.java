package academy.devdojo.controller;


import academy.devdojo.commons.FileUtils;
import academy.devdojo.commons.UserUtils;
import academy.devdojo.model.User;
import academy.devdojo.repository.UserData;
import academy.devdojo.repository.UserRepositoryHardcoded;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

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

    @SpyBean
    private UserRepositoryHardcoded repository;

    @Autowired
    private UserUtils userUtils;

    private List<User> userList;

    @BeforeEach
    void setup() {
        userList = userUtils.createNewUserList();
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

        var userToSave = userUtils.createNewUser();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(userToSave);


        var request = fileUtils.readFromFile(RESOURCES_DIRECTORY + "/request/post-request-user-200.json");
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

        var request = fileUtils.readFromFile(RESOURCES_DIRECTORY + "/request/put-request-user-200.json");

        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        mockMvc.perform(put(URL).content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("PUT /v1/users throws ResponseStatusException when id is not found")
    @Order(10)
    void update_ThrowsResponseStatusException404_WhenIdIsNotFound() throws Exception {

        var request = fileUtils.readFromFile(RESOURCES_DIRECTORY + "/request/put-request-user-404.json");

        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        mockMvc.perform(put(URL).content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @ParameterizedTest
    @MethodSource("postUserBadRequestSource")
    @DisplayName("POST /v1/users returns Bad Request when fields are blank")
    @Order(11)
    void save_ReturnsBadRequest_WhenFieldsAreBlank(String filename, List<String> errorMessages) throws Exception {

        var request = fileUtils.readFromFile(RESOURCES_DIRECTORY + "/request/" + filename);

        var mvcResult = mockMvc.perform(post(URL).content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        var errorMessage = mvcResult.getResolvedException();

        Assertions.assertThat(errorMessage).isNotNull();

        Assertions.assertThat(errorMessage.getMessage()).contains(errorMessages);

    }

    @ParameterizedTest
    @MethodSource("putUserBadRequestSource")
    @DisplayName("PUT /v1/users returns Bad Request when fields are invalid")
    @Order(12)
    void update_ReturnsBadRequest_WhenFieldsAreBlankOrEmailInvalid(String filename, List<String> errorMessages) throws Exception {

        var request = fileUtils.readFromFile(RESOURCES_DIRECTORY + "/request/" + filename);

        var mvcResult = mockMvc.perform(put(URL).content(request).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        var errorMessage = mvcResult.getResolvedException();

        Assertions.assertThat(errorMessage).isNotNull();

        Assertions.assertThat(errorMessage.getMessage()).contains(errorMessages);

    }


    private static Stream<Arguments> postUserBadRequestSource() {


        var emailInvalidError = invalidEmailError();

        var blankErrors = allBlankErrors();


        return Stream.of(
                Arguments.of("post-request-user-blank-fields-400.json", blankErrors),
                Arguments.of("post-request-user-empty-fields-400.json", blankErrors),
                Arguments.of("post-request-user-invalid-email-400.json", emailInvalidError)
        );
    }

    private static Stream<Arguments> putUserBadRequestSource() {

        var emailInvalidError = invalidEmailError();
        var idNullError = nullIdError();

        var blankErrors = allBlankErrors();

        return Stream.of(
                Arguments.of("put-request-user-blank-fields-400.json", blankErrors),
                Arguments.of("put-request-user-empty-fields-400.json", blankErrors),
                Arguments.of("put-request-user-invalid-email-400.json", emailInvalidError),
                Arguments.of("put-request-user-null-id-400.json", idNullError)
        );

    }

    private static List<String> allBlankErrors() {
        var firstNameBlankError = "Attribute 'firstName' is required";
        var lastNameBlankError = "Attribute 'lastName' is required";
        var emailBlankError = "Attribute 'email' is required";

        return List.of(firstNameBlankError, lastNameBlankError, emailBlankError);
    }

    private static List<String> invalidEmailError() {
        var emailInvalidError = "The email is not valid";
        return List.of(emailInvalidError);

    }

    private static List<String> nullIdError() {
        var nullIdError = "Attribute 'id' shouldn't be null";
        return List.of(nullIdError);
    }


}