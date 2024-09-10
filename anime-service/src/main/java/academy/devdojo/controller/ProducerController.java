package academy.devdojo.controller;


import academy.devdojo.mapper.ProducerMapper;
import academy.devdojo.request.ProducerPostRequest;
import academy.devdojo.request.ProducerPutRequest;
import academy.devdojo.response.ProducerGetResponse;
import academy.devdojo.response.ProducerPostResponse;
import academy.devdojo.service.ProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("v1/producers")
@Slf4j


public class ProducerController {

    private ProducerService producerService;

    private static final ProducerMapper MAPPER = ProducerMapper.INSTANCE;

    public ProducerController() {
        this.producerService = new ProducerService();
    }

    @GetMapping
    public ResponseEntity<List<ProducerGetResponse>> listAll(@RequestParam(defaultValue = "") String name) {

        var producers = producerService.findAll(name);

        var producerGetResponseList = MAPPER.toProducerGetResponseList(producers);

        return ResponseEntity.ok(producerGetResponseList);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id) {

        var producer = producerService.findByIdOrThrowNotFound(id);

        var producerGetResponse = MAPPER.toProducerGetResponse(producer);

        return ResponseEntity.ok(producerGetResponse);
    }

    @PostMapping
    public ResponseEntity<ProducerPostResponse> save(@RequestBody ProducerPostRequest producerPostRequest) {

        log.debug("Request to save producer: '{}'", producerPostRequest);

        var producer = MAPPER.toProducer(producerPostRequest);

        var producerSaved = producerService.save(producer);

        var producerPostResponse = MAPPER.toProducerPostResponse(producerSaved);

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

        var producer = MAPPER.toProducer(producerPutRequest);

        producerService.update(producer);


        return ResponseEntity.noContent().build();
    }


}
