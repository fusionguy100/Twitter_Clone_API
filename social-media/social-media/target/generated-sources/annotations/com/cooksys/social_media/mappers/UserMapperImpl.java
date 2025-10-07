package com.cooksys.social_media.mappers;

import com.cooksys.social_media.dtos.UserRequestDto;
import com.cooksys.social_media.dtos.UserResponseDto;
import com.cooksys.social_media.entities.User;
import com.cooksys.social_media.entities.embeddables.Credentials;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-23T10:41:22-0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private ProfileMapper profileMapper;
    @Autowired
    private CredentialsMapper credentialsMapper;

    @Override
    public UserResponseDto entityToDto(User entity) {
        if ( entity == null ) {
            return null;
        }

        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setUsername( entityCredentialsUsername( entity ) );
        userResponseDto.setProfile( profileMapper.entityToDto( entity.getProfile() ) );
        userResponseDto.setJoined( entity.getJoined() );

        return userResponseDto;
    }

    @Override
    public List<UserResponseDto> entitiesToDtos(List<User> entities) {
        if ( entities == null ) {
            return null;
        }

        List<UserResponseDto> list = new ArrayList<UserResponseDto>( entities.size() );
        for ( User user : entities ) {
            list.add( entityToDto( user ) );
        }

        return list;
    }

    @Override
    public User dtoToEntity(UserRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setProfile( profileMapper.dtoToEntity( dto.getProfile() ) );
        user.setCredentials( credentialsMapper.dtoToEntity( dto.getCredentials() ) );

        return user;
    }

    private String entityCredentialsUsername(User user) {
        Credentials credentials = user.getCredentials();
        if ( credentials == null ) {
            return null;
        }
        return credentials.getUsername();
    }
}
