package academy.devdojo.request.anime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AnimePostRequest(@NotBlank(message = "The field 'name' is required") String name,
                               @NotNull(message = "The field 'episodes' is required") Long episodes) {
}
