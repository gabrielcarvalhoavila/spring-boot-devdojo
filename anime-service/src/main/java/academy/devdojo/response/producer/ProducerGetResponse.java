package academy.devdojo.response.producer;

import java.time.LocalDateTime;

public record ProducerGetResponse(Long id, String name, LocalDateTime createdAt) {
}
