package academy.devdojo.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserPutRequest(
        @NotNull(message = "Attribute 'id' shouldn't be null") Long id,
        @NotBlank(message = "Attribute 'firstName' is required") String firstName,
        @NotBlank(message = "Attribute 'lastName' is required") String lastName,
        @Email(message = "The email is not valid") @NotBlank(message = "Attribute 'email' is required") String email) {
}
