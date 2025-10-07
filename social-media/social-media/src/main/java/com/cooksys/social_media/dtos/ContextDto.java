package com.cooksys.social_media.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ContextDto {

    private TweetResponseDto target;

    private List<TweetResponseDto> before;

    private List<TweetResponseDto> after;

}
