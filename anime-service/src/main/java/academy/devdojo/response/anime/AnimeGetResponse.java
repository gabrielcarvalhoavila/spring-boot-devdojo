package academy.devdojo.response.anime;

import java.time.LocalDateTime;

public record AnimeGetResponse(Long id, String name, Long episodes, LocalDateTime createdAt) {
}
