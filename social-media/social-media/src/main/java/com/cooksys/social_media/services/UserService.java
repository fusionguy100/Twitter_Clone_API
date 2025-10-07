package com.cooksys.social_media.services;

import com.cooksys.social_media.dtos.CredentialsDto;
import com.cooksys.social_media.dtos.TweetResponseDto;
import com.cooksys.social_media.dtos.UserRequestDto;
import com.cooksys.social_media.dtos.UserResponseDto;

import java.util.List;

public interface UserService {
    List<UserResponseDto> getAllUsers();

    UserResponseDto createUser(UserRequestDto userRequestDto);

    UserResponseDto getUserByUsername(String username);

    UserResponseDto deleteUserByUsername(String username, CredentialsDto credentialsDto);

    UserResponseDto updateUserByUsername(String username, UserRequestDto userRequestDto);

    void followUser(String username, CredentialsDto credentialsDto);

    void unfollowUser(String username, CredentialsDto credentialsDto);

    List<TweetResponseDto> getUserFeed(String username);

    List<TweetResponseDto> getUserTweets(String username);

    List<TweetResponseDto> getUserMentions(String username);

    List<UserResponseDto> getUserFollowers(String username);

    List<UserResponseDto> getUserFollowing(String username);

}
