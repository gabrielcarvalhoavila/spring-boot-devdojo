package academy.devdojo.request.anime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AnimePutRequest(@NotNull(message = "The field 'id' is required") Long id,
                              @NotBlank(message = "The field 'name' is required") String name,
                              @NotNull(message = "The field 'episodes' is required") Long episodes) {
}
