package com.cooksys.social_media.mappers;

import com.cooksys.social_media.dtos.TweetRequestDto;
import com.cooksys.social_media.dtos.TweetResponseDto;
import com.cooksys.social_media.entities.Tweet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { UserMapper.class, CredentialsMapper.class, HashtagMapper.class, ProfileMapper.class, TweetMapper.class })
public interface TweetMapper {

//    @Mapping(source="rePostOf", target="repostOf")
    TweetResponseDto entityToDto(Tweet entity);

    List<TweetResponseDto> entitiesToDtos(List<Tweet> entities);

//    @Mapping(source="repostOf", target="rePostOf")
    Tweet dtoToEntity(TweetRequestDto dto);
}
