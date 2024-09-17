package academy.devdojo.repository;

import academy.devdojo.model.Producer;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class ProducerData {

    private final List<Producer> producers = new ArrayList<>();

    {
        producers.add(Producer.builder().id(1L).name("Studio Ghibli").createdAt(LocalDateTime.now()).build());
        producers.add(Producer.builder().id(2L).name("Madhouse").createdAt(LocalDateTime.now()).build());
        producers.add(Producer.builder().id(3L).name("Bones").createdAt(LocalDateTime.now()).build());
        producers.add(Producer.builder().id(4L).name("Toei Animation").createdAt(LocalDateTime.now()).build());
        producers.add(Producer.builder().id(5L).name("Sunrise").createdAt(LocalDateTime.now()).build());
    }

}
