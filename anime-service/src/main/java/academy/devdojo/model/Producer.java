package academy.devdojo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class Producer {
    private Long id;
    private String name;
    @Getter
    private static List<Producer> producerList= new ArrayList<>();
    private LocalDateTime createdAt;


    static {
        producerList.add(Producer.builder().id(1L).name("Studio Ghibli").createdAt(LocalDateTime.now()).build());
        producerList.add(Producer.builder().id(2L).name("Madhouse").createdAt(LocalDateTime.now()).build());
        producerList.add(Producer.builder().id(3L).name("Bones").createdAt(LocalDateTime.now()).build());
        producerList.add(Producer.builder().id(4L).name("Toei Animation").createdAt(LocalDateTime.now()).build());
        producerList.add(Producer.builder().id(5L).name("Sunrise").createdAt(LocalDateTime.now()).build());
    }



}
