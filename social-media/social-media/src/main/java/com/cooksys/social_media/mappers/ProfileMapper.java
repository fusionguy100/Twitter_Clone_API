package com.cooksys.social_media.mappers;

import com.cooksys.social_media.dtos.ProfileDto;
import com.cooksys.social_media.entities.embeddables.Profile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileDto entityToDto(Profile entity);
    List<ProfileDto> entitiesToDtos(List<Profile> entities);
    Profile dtoToEntity(ProfileDto dto);
}
