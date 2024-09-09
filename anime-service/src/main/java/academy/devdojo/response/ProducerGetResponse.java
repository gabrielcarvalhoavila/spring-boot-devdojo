package academy.devdojo.response;

import java.time.LocalDateTime;

public record ProducerGetResponse(Long id, String name, LocalDateTime createdAt) {
}
