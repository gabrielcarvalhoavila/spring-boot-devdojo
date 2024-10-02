package academy.devdojo.request.producer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProducerPutRequest(@NotNull(message = "Attribute 'id' is required") Long id,
                                 @NotBlank(message = "Attribute 'name' is required") String name) {
}
