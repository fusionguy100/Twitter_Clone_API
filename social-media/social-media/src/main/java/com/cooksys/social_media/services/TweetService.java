package com.cooksys.social_media.services;

import com.cooksys.social_media.dtos.*;

import java.util.List;

public interface TweetService {
    List<TweetResponseDto> getTweets();

    TweetResponseDto postTweet(TweetRequestDto tweetRequestDto);

    TweetResponseDto getTweetById(Long id);

    TweetResponseDto deleteTweetById(Long id, CredentialsDto credentialsDto);

    void likeTweetById(Long id, CredentialsDto credentialsDto);

    TweetResponseDto replyToTweetById(Long id, TweetRequestDto tweetRequestDto);

    List<HashtagDto> getTagsByTweetId(Long id);

    ContextDto getContextByTweetId(Long id);

    List<UserResponseDto> getLikesByTweetId(Long id);

    List<TweetResponseDto> getRepliesByTweetId(Long id);

    List<TweetResponseDto> getRepostsByTweetId(Long id);

    List<UserResponseDto> getMentionsByTweetId(Long id);

    TweetResponseDto repostTweetById(Long id, CredentialsDto credentialsDto);
}
