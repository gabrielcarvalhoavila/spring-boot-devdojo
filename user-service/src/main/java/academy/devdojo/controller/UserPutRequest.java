package academy.devdojo.controller;

public record UserPutRequest(Long id, String firstName, String lastName, String email) {
}
