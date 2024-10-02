package academy.devdojo.request.producer;

import jakarta.validation.constraints.NotBlank;

public record ProducerPostRequest(@NotBlank(message = "Attribute 'name' is required") String name) {
}
