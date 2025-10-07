package com.cooksys.social_media.mappers;

import com.cooksys.social_media.dtos.CredentialsDto;
import com.cooksys.social_media.entities.embeddables.Credentials;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-23T10:41:22-0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
@Component
public class CredentialsMapperImpl implements CredentialsMapper {

    @Override
    public CredentialsDto entityToDto(Credentials entity) {
        if ( entity == null ) {
            return null;
        }

        CredentialsDto credentialsDto = new CredentialsDto();

        credentialsDto.setUsername( entity.getUsername() );
        credentialsDto.setPassword( entity.getPassword() );

        return credentialsDto;
    }

    @Override
    public List<CredentialsDto> entitiesToDtos(List<Credentials> entities) {
        if ( entities == null ) {
            return null;
        }

        List<CredentialsDto> list = new ArrayList<CredentialsDto>( entities.size() );
        for ( Credentials credentials : entities ) {
            list.add( entityToDto( credentials ) );
        }

        return list;
    }

    @Override
    public Credentials dtoToEntity(CredentialsDto dto) {
        if ( dto == null ) {
            return null;
        }

        Credentials credentials = new Credentials();

        credentials.setUsername( dto.getUsername() );
        credentials.setPassword( dto.getPassword() );

        return credentials;
    }
}
