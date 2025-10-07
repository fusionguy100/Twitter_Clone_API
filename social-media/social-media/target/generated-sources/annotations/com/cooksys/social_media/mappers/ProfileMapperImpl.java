package com.cooksys.social_media.mappers;

import com.cooksys.social_media.dtos.ProfileDto;
import com.cooksys.social_media.entities.embeddables.Profile;
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
public class ProfileMapperImpl implements ProfileMapper {

    @Override
    public ProfileDto entityToDto(Profile entity) {
        if ( entity == null ) {
            return null;
        }

        ProfileDto profileDto = new ProfileDto();

        profileDto.setFirstName( entity.getFirstName() );
        profileDto.setLastName( entity.getLastName() );
        profileDto.setEmail( entity.getEmail() );
        profileDto.setPhone( entity.getPhone() );

        return profileDto;
    }

    @Override
    public List<ProfileDto> entitiesToDtos(List<Profile> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ProfileDto> list = new ArrayList<ProfileDto>( entities.size() );
        for ( Profile profile : entities ) {
            list.add( entityToDto( profile ) );
        }

        return list;
    }

    @Override
    public Profile dtoToEntity(ProfileDto dto) {
        if ( dto == null ) {
            return null;
        }

        Profile profile = new Profile();

        profile.setFirstName( dto.getFirstName() );
        profile.setLastName( dto.getLastName() );
        profile.setEmail( dto.getEmail() );
        profile.setPhone( dto.getPhone() );

        return profile;
    }
}
