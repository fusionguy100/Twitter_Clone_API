package com.cooksys.social_media.services;

import com.cooksys.social_media.dtos.HashtagDto;
import com.cooksys.social_media.dtos.TweetResponseDto;

import java.util.List;

public interface HashtagService {
    List<HashtagDto> getAllTags();

    List<TweetResponseDto> getTag(String label);
}
