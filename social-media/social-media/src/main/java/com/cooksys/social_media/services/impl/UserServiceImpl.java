package com.cooksys.social_media.services.impl;

import com.cooksys.social_media.dtos.*;
import com.cooksys.social_media.entities.Tweet;
import com.cooksys.social_media.entities.User;
import com.cooksys.social_media.exceptions.BadRequestException;
import com.cooksys.social_media.exceptions.NotAuthorizedException;
import com.cooksys.social_media.exceptions.NotFoundException;
import com.cooksys.social_media.mappers.ProfileMapper;
import com.cooksys.social_media.mappers.TweetMapper;
import com.cooksys.social_media.mappers.UserMapper;
import com.cooksys.social_media.respositories.TweetRepository;
import com.cooksys.social_media.respositories.UserRepository;
import com.cooksys.social_media.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProfileMapper profileMapper;
    private final TweetMapper tweetMapper;

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userMapper.entitiesToDtos(userRepository.findAllByDeletedIsFalse());
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        CredentialsDto credentials = userRequestDto.getCredentials();
        ProfileDto profile = userRequestDto.getProfile();
        if (credentials == null || credentials.getUsername() == null || credentials.getUsername().isBlank()
                || credentials.getPassword() == null || credentials.getPassword().isBlank()) {
            throw new BadRequestException("Username and password are required");
        }
        if (profile == null || profile.getEmail() == null || profile.getEmail().isBlank()) {
            throw new BadRequestException("Email in profile is required");
        }
        if (userRepository.existsByCredentialsUsernameAndDeletedIsFalse(credentials.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }
        if (userRepository.existsByCredentialsUsernameAndDeletedIsTrue(credentials.getUsername())) {
             User user = userRepository.findByCredentialsUsernameAndCredentialsPassword(credentials.getUsername(),
                     credentials.getPassword());
             user.setDeleted(false);
             user.setProfile(profileMapper.dtoToEntity(userRequestDto.getProfile()));
             return userMapper.entityToDto(userRepository.save(user));
        }
        User user = userRepository.save(userMapper.dtoToEntity(userRequestDto));

        return userMapper.entityToDto(user);
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        if (!userRepository.existsByCredentialsUsernameAndDeletedIsFalse(username)) {
            throw new NotFoundException("User not found");
        }
        User user = userRepository.findByCredentialsUsernameAndDeletedIsFalse(username);
        return userMapper.entityToDto(user);
    }

    @Override
    public UserResponseDto updateUserByUsername(String username, UserRequestDto userRequestDto) {
        if (!userRepository.existsByCredentialsUsernameAndDeletedIsFalse(username)) {
            throw new NotFoundException("User not found");
        }
        CredentialsDto credentials = userRequestDto.getCredentials();
        ProfileDto profile = userRequestDto.getProfile();
        if (credentials == null || credentials.getUsername() == null || credentials.getUsername().isBlank()
                || credentials.getPassword() == null || credentials.getPassword().isBlank()) {
            throw new BadRequestException("Username and password are required");
        }
        if (profile == null) {
            throw new BadRequestException("Profile is required");
        }
        User user = userRepository.findByCredentialsUsernameAndCredentialsPassword(credentials.getUsername(),
                credentials.getPassword());
        if (user == null || !user.getCredentials().getUsername().equals(username)) {
            throw new NotAuthorizedException("Invalid credentials");
        }
        if (profile.getPhone() != null) user.getProfile().setPhone(profile.getPhone());
        if (profile.getEmail() != null) user.getProfile().setEmail(profile.getEmail());
        if (profile.getFirstName() != null) user.getProfile().setFirstName(profile.getFirstName());
        if (profile.getLastName() != null) user.getProfile().setLastName(profile.getLastName());
        return userMapper.entityToDto(userRepository.save(user));

    }

    @Override
    public UserResponseDto deleteUserByUsername(String username, CredentialsDto credentialsDto) {
        if (!userRepository.existsByCredentialsUsernameAndDeletedIsFalse(username)) {
            throw new NotFoundException("User not found");
        }
        User user = userRepository.findByCredentialsUsernameAndCredentialsPassword(credentialsDto.getUsername(),
                credentialsDto.getPassword());
        if (user == null || !user.getCredentials().getUsername().equals(username)) {
            throw new NotAuthorizedException("Invalid credentials");
        }
        user.setDeleted(true);
        return userMapper.entityToDto(userRepository.save(user));

    }

    @Override
    public void followUser(String username, CredentialsDto credentialsDto) {
        User user = userRepository.findByCredentialsUsernameAndCredentialsPassword(credentialsDto.getUsername(),
                credentialsDto.getPassword());
        if (user == null) {
            throw new NotAuthorizedException("Invalid credentials");
        }
        if (!userRepository.existsByCredentialsUsernameAndDeletedIsFalse(username)) {
            throw new NotFoundException("User not found");
        }
        User userToFollow = userRepository.findByCredentialsUsernameAndDeletedIsFalse(username);
        if (user.getFollowing().contains(userToFollow) || user.getCredentials().getUsername().equals(username)) {
            throw new BadRequestException("You are already following this user or trying to follow yourself");
        }
        user.getFollowing().add(userToFollow);
        userRepository.saveAndFlush(user);
    }

    @Override
    public void unfollowUser(String username, CredentialsDto credentialsDto) {
        User user = userRepository.findByCredentialsUsernameAndCredentialsPassword(credentialsDto.getUsername(),
                credentialsDto.getPassword());
        if (user == null) {
            throw new NotAuthorizedException("Invalid credentials");
        }
        if (!userRepository.existsByCredentialsUsernameAndDeletedIsFalse(username)) {
            throw new NotFoundException("User not found");
        }
        User userToUnfollow = userRepository.findByCredentialsUsernameAndDeletedIsFalse(username);
        if (!user.getFollowing().contains(userToUnfollow) || user.getCredentials().getUsername().equals(username)) {
            throw new BadRequestException("You are not following this user or trying to unfollow yourself");
        }
        user.getFollowing().remove(userToUnfollow);
        userRepository.saveAndFlush(user);
    }

    @Override
    public List<TweetResponseDto> getUserFeed(String username) {
        if (!userRepository.existsByCredentialsUsernameAndDeletedIsFalse(username)) {
            throw new NotFoundException("User not found");
        }
        User user = userRepository.findByCredentialsUsernameAndDeletedIsFalse(username);
        return tweetMapper.entitiesToDtos(
    (Stream.concat(
                user.getTweets().stream(),
                user.getFollowing().stream().flatMap(u -> u.getTweets().stream())
        ).filter(t -> !t.isDeleted())
                .sorted((t1, t2) -> t2.getPosted().compareTo(t1.getPosted()))
                .toList()));

    }

    @Override
    public List<TweetResponseDto> getUserTweets(String username) {
        if (!userRepository.existsByCredentialsUsernameAndDeletedIsFalse(username)) {
            throw new NotFoundException("User not found");
        }
        User user = userRepository.findByCredentialsUsernameAndDeletedIsFalse(username);
        return tweetMapper.entitiesToDtos(user.getTweets().stream().filter(t -> !t.isDeleted())
                .sorted((t1, t2) -> t2.getPosted().compareTo(t1.getPosted()))
                .toList());
    }

    @Override
    public List<TweetResponseDto> getUserMentions(String username) {
        if (!userRepository.existsByCredentialsUsernameAndDeletedIsFalse(username)) {
            throw new NotFoundException("User not found");
        }
        User user = userRepository.findByCredentialsUsernameAndDeletedIsFalse(username);
        return tweetMapper.entitiesToDtos(user.getMentionedIn().stream().filter(t -> !t.isDeleted()).toList());
    }

    @Override
    public List<UserResponseDto> getUserFollowers(String username) {
        if (!userRepository.existsByCredentialsUsernameAndDeletedIsFalse(username)) {
            throw new NotFoundException("User not found");
        }
        User user = userRepository.findByCredentialsUsernameAndDeletedIsFalse(username);
        return userMapper.entitiesToDtos(new ArrayList<>(user.getFollowers()).stream().filter(u -> !u.isDeleted()).toList());
    }

    @Override
    public List<UserResponseDto> getUserFollowing(String username) {
        if (!userRepository.existsByCredentialsUsernameAndDeletedIsFalse(username)) {
            throw new NotFoundException("User not found");
        }
        User user = userRepository.findByCredentialsUsernameAndDeletedIsFalse(username);
        return userMapper.entitiesToDtos(new ArrayList<>(user.getFollowing()).stream().filter(u -> !u.isDeleted()).toList());
    }
}
