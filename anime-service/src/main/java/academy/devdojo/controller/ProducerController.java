package academy.devdojo.controller;


import academy.devdojo.model.Producer;
import academy.devdojo.request.ProducerPostRequest;
import academy.devdojo.response.ProducerPostResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@RestController
@RequestMapping("v1/producers")
@Slf4j


public class ProducerController {


    @GetMapping
    public List<Producer> list() {
        return Producer.getProducerList();
    }

    @GetMapping("list2")
    public List<Producer> filterByName(@RequestParam(defaultValue = "") String name) {
        return Producer.getProducerList().stream().filter(producer -> producer.getName().contains(name)).toList();
    }

    @GetMapping("/{id}")
    public List<Producer> list3(@PathVariable Long id) {
        return Producer.getProducerList().stream().filter(producer -> producer.getId().equals(id)).toList();
    }

    @PostMapping
    public ResponseEntity<ProducerPostResponse> save(@RequestBody ProducerPostRequest producerPostRequest) {
        Long id = ThreadLocalRandom.current().nextLong(1, 1000);

        var producer = Producer.builder().id(id).name(producerPostRequest.name()).createdAt(LocalDateTime.now()).build();
        Producer.getProducerList().add(producer);

        var response = new ProducerPostResponse(producer.getId(), producer.getName(), producer.getCreatedAt());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
