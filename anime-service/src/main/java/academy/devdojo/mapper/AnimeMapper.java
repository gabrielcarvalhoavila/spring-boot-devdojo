package academy.devdojo.mapper;

import academy.devdojo.model.Anime;
import academy.devdojo.request.anime.AnimePostRequest;
import academy.devdojo.request.anime.AnimePutRequest;
import academy.devdojo.response.anime.AnimeGetResponse;
import academy.devdojo.response.anime.AnimePostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(1, 1000))")
    Anime toAnime(AnimePostRequest animePostRequest);

    Anime toAnime(AnimePutRequest animePutRequest);

    AnimeGetResponse toAnimeGetResponse(Anime anime);

    List<AnimeGetResponse> toAnimeGetResponseList(List<Anime> animeList);

    AnimePostResponse toAnimePostResponse(Anime anime);
}
