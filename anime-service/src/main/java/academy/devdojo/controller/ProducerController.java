package academy.devdojo.controller;


import academy.devdojo.mapper.ProducerMapper;
import academy.devdojo.request.producer.ProducerPostRequest;
import academy.devdojo.request.producer.ProducerPutRequest;
import academy.devdojo.response.producer.ProducerGetResponse;
import academy.devdojo.response.producer.ProducerPostResponse;
import academy.devdojo.service.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("v1/producers")
@Slf4j
@RequiredArgsConstructor

public class ProducerController {

    private final ProducerService producerService;
    private final ProducerMapper mapper;


    @GetMapping
    public ResponseEntity<List<ProducerGetResponse>> listAll(@RequestParam(defaultValue = "") String name) {

        var producers = producerService.findAll(name);

        var producerGetResponseList = mapper.toProducerGetResponseList(producers);

        return ResponseEntity.ok(producerGetResponseList);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id) {

        var producer = producerService.findByIdOrThrowNotFound(id);

        var producerGetResponse = mapper.toProducerGetResponse(producer);

        return ResponseEntity.ok(producerGetResponse);
    }

    @PostMapping
    public ResponseEntity<ProducerPostResponse> save(@RequestBody ProducerPostRequest producerPostRequest) {

        log.debug("Request to save producer: '{}'", producerPostRequest);

        var producer = mapper.toProducer(producerPostRequest);

        var producerSaved = producerService.save(producer);

        var producerPostResponse = mapper.toProducerPostResponse(producerSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(producerPostResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {

        log.debug("Deleting producer with id {}", id);

        producerService.delete(id);

        return ResponseEntity.noContent().build();

    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody ProducerPutRequest producerPutRequest) {

        log.debug("Request to update producer: '{}'", producerPutRequest);

        var producer = mapper.toProducer(producerPutRequest);

        producerService.update(producer);


        return ResponseEntity.noContent().build();
    }


}
