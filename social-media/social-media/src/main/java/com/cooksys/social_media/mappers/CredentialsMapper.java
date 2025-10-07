package com.cooksys.social_media.mappers;

import com.cooksys.social_media.dtos.CredentialsDto;
import com.cooksys.social_media.entities.embeddables.Credentials;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {

    CredentialsDto entityToDto(Credentials entity);
    List<CredentialsDto> entitiesToDtos(List<Credentials> entities);
    Credentials dtoToEntity(CredentialsDto dto);
}
