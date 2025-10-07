package com.cooksys.social_media.mappers;

import com.cooksys.social_media.dtos.UserRequestDto;
import com.cooksys.social_media.dtos.UserResponseDto;
import com.cooksys.social_media.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { ProfileMapper.class, CredentialsMapper.class, TweetMapper.class })
public interface UserMapper {

    @Mapping(source="credentials.username", target="username")
    UserResponseDto entityToDto(User entity);

    List<UserResponseDto> entitiesToDtos(List<User> entities);

    User dtoToEntity(UserRequestDto dto);

}
