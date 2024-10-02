package academy.devdojo.controller;

import academy.devdojo.controller.request.UserPostRequest;
import academy.devdojo.controller.request.UserPutRequest;
import academy.devdojo.controller.response.UserGetResponse;
import academy.devdojo.controller.response.UserPostResponse;
import academy.devdojo.mapper.UserMapper;
import academy.devdojo.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor

public class UserController {

    private final UserMapper mapper;
    private final UserService service;


    @GetMapping
    public ResponseEntity<List<UserGetResponse>> findAll(
            @Valid @RequestParam(required = false) @Size(min = 2) String firstName) {


        var users = service.findAll(firstName);

        var userGetResponseList = mapper.toUserGetResponseList(users);

        return ResponseEntity.ok(userGetResponseList);

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetResponse> findById(@PathVariable Long id) {

        var user = service.findByIdOrThrowException(id);

        var response = mapper.toUserGetResponse(user);

        return ResponseEntity.ok(response);

    }

    @PostMapping
    public ResponseEntity<UserPostResponse> save(@Valid @RequestBody UserPostRequest userPostRequest) {

        var userToSave = mapper.toUser(userPostRequest);

        var userSaved = service.save(userToSave);

        var response = mapper.toUserPostResponse(userSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PutMapping
    public ResponseEntity<Void> update(@Valid @RequestBody UserPutRequest userPutRequest) {

        var userToUpdate = mapper.toUser(userPutRequest);

        service.update(userToUpdate);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }


}
