package academy.devdojo.response;

import java.time.LocalDateTime;

public record AnimeGetResponse(Long id, String name, Long episodes, LocalDateTime createdAt) {
}
