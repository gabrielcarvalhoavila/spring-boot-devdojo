package academy.devdojo.controller;


import academy.devdojo.mapper.ProducerMapper;
import academy.devdojo.model.Producer;
import academy.devdojo.request.ProducerPostRequest;
import academy.devdojo.response.ProducerGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("v1/producers")
@Slf4j


public class ProducerController {

    private static final ProducerMapper MAPPER = ProducerMapper.INSTANCE;

    @GetMapping
    public ResponseEntity<List<ProducerGetResponse>> list(@RequestParam(defaultValue = "") String name) {
        if (name == null) {
            var producerGetResponseList = Producer.getProducerList().stream()
                    .map(MAPPER::toProducerGetResponse)
                    .toList();

            return ResponseEntity.ok(producerGetResponseList);
        }

        var producerGetResponseList = Producer.getProducerList().stream()
                .filter(producer -> producer.getName().contains(name))
                .map(MAPPER::toProducerGetResponse)
                .toList();

        return ResponseEntity.ok(producerGetResponseList);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id) {

        var producerGetResponse = Producer.getProducerList()
                .stream()
                .filter(producer -> producer.getId().equals(id))
                .map(MAPPER::toProducerGetResponse)
                .findFirst()
                .orElse(null);

        return ResponseEntity.ok(producerGetResponse);
    }

    @PostMapping
    public ResponseEntity<ProducerGetResponse> save(@RequestBody ProducerPostRequest producerPostRequest) {
        var producer = MAPPER.toProducer(producerPostRequest);
        var response = MAPPER.toProducerGetResponse(producer);

        Producer.getProducerList().add(producer);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
