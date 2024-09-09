package academy.devdojo.response;

import java.time.LocalDateTime;

public record ProducerPostResponse(Long id, String name, LocalDateTime createdAt) {
}
