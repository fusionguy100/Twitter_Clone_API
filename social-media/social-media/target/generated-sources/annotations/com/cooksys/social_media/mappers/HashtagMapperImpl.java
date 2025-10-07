package com.cooksys.social_media.mappers;

import com.cooksys.social_media.dtos.HashtagDto;
import com.cooksys.social_media.entities.Hashtag;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-23T10:41:21-0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
@Component
public class HashtagMapperImpl implements HashtagMapper {

    @Override
    public HashtagDto entityToDto(Hashtag entity) {
        if ( entity == null ) {
            return null;
        }

        HashtagDto hashtagDto = new HashtagDto();

        hashtagDto.setLabel( entity.getLabel() );
        hashtagDto.setFirstUsed( entity.getFirstUsed() );
        hashtagDto.setLastUsed( entity.getLastUsed() );

        return hashtagDto;
    }

    @Override
    public List<HashtagDto> entitiesToDtos(List<Hashtag> entities) {
        if ( entities == null ) {
            return null;
        }

        List<HashtagDto> list = new ArrayList<HashtagDto>( entities.size() );
        for ( Hashtag hashtag : entities ) {
            list.add( entityToDto( hashtag ) );
        }

        return list;
    }

    @Override
    public Hashtag dtoToEntity(HashtagDto dto) {
        if ( dto == null ) {
            return null;
        }

        Hashtag hashtag = new Hashtag();

        hashtag.setLabel( dto.getLabel() );
        hashtag.setFirstUsed( dto.getFirstUsed() );
        hashtag.setLastUsed( dto.getLastUsed() );

        return hashtag;
    }
}
