package com.cooksys.social_media.mappers;

import com.cooksys.social_media.dtos.TweetRequestDto;
import com.cooksys.social_media.dtos.TweetResponseDto;
import com.cooksys.social_media.entities.Tweet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-23T13:40:52-0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
@Component
public class TweetMapperImpl implements TweetMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public TweetResponseDto entityToDto(Tweet entity) {
        if ( entity == null ) {
            return null;
        }

        TweetResponseDto tweetResponseDto = new TweetResponseDto();

        tweetResponseDto.setId( entity.getId() );
        tweetResponseDto.setAuthor( userMapper.entityToDto( entity.getAuthor() ) );
        tweetResponseDto.setPosted( entity.getPosted() );
        tweetResponseDto.setContent( entity.getContent() );
        tweetResponseDto.setInReplyTo( entityToDto( entity.getInReplyTo() ) );
        tweetResponseDto.setRepostOf( entityToDto( entity.getRepostOf() ) );

        return tweetResponseDto;
    }

    @Override
    public List<TweetResponseDto> entitiesToDtos(List<Tweet> entities) {
        if ( entities == null ) {
            return null;
        }

        List<TweetResponseDto> list = new ArrayList<TweetResponseDto>( entities.size() );
        for ( Tweet tweet : entities ) {
            list.add( entityToDto( tweet ) );
        }

        return list;
    }

    @Override
    public Tweet dtoToEntity(TweetRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Tweet tweet = new Tweet();

        tweet.setContent( dto.getContent() );

        return tweet;
    }
}
