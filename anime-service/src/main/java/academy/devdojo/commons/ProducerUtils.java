package academy.devdojo.commons;

import academy.devdojo.model.Producer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProducerUtils {

    public List<Producer> getNewProducers() {
        var dateTime = "2024-09-17T09:23:25.125372";
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        var localDateTime = LocalDateTime.parse(dateTime, formatter);

        var ufotable = Producer.builder().id(1L).name("Ufotable").createdAt(localDateTime).build();
        var kyotoAnimation = Producer.builder().id(2L).name("Kyoto Animation").createdAt(localDateTime).build();
        var a1Pictures = Producer.builder().id(3L).name("A-1 Pictures").createdAt(localDateTime).build();
        var witStudio = Producer.builder().id(4L).name("Wit Studio").createdAt(localDateTime).build();
        var trigger = Producer.builder().id(5L).name("Trigger").createdAt(localDateTime).build();

        return new ArrayList<>(List.of(ufotable, kyotoAnimation, a1Pictures, witStudio, trigger));
    }

    public Producer getNewProducer() {
        return Producer.builder().id(6L).name("Aniplex").createdAt(LocalDateTime.now()).build();
    }
}
