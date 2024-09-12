package academy.devdojo.mapper;

import academy.devdojo.model.Producer;
import academy.devdojo.request.producer.ProducerPostRequest;
import academy.devdojo.request.producer.ProducerPutRequest;
import academy.devdojo.response.producer.ProducerGetResponse;
import academy.devdojo.response.producer.ProducerPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProducerMapper {

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(1, 1000))")
    Producer toProducer(ProducerPostRequest producerPostRequest);

    Producer toProducer(ProducerPutRequest producerPutRequest);

    ProducerGetResponse toProducerGetResponse(Producer producer);

    ProducerPostResponse toProducerPostResponse(Producer producer);

    List<ProducerGetResponse> toProducerGetResponseList(List<Producer> producers);
}
