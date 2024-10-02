package academy.devdojo.mapper;

import academy.devdojo.controller.request.UserPutRequest;
import academy.devdojo.controller.request.UserPostRequest;
import academy.devdojo.controller.response.UserGetResponse;
import academy.devdojo.controller.response.UserPostResponse;
import academy.devdojo.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(1, 1000))")
    User toUser(UserPostRequest userPostRequest);

    User toUser(UserPutRequest userPutRequest);

    UserGetResponse toUserGetResponse(User user);

    UserPostResponse toUserPostResponse(User user);

    List<UserGetResponse> toUserGetResponseList(List<User> user);
}
