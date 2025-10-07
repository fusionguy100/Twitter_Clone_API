package com.cooksys.social_media.controllers;

import com.cooksys.social_media.dtos.CredentialsDto;
import com.cooksys.social_media.dtos.TweetResponseDto;
import com.cooksys.social_media.dtos.UserRequestDto;
import com.cooksys.social_media.dtos.UserResponseDto;
import com.cooksys.social_media.entities.User;
import com.cooksys.social_media.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    @GetMapping
    public List<UserResponseDto> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }

    @GetMapping("/@{username}")
    public UserResponseDto getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @PatchMapping("/@{username}")
    public UserResponseDto updateUserByUsername(@PathVariable String username,
                                                @RequestBody UserRequestDto userRequestDto) {
        return userService.updateUserByUsername(username, userRequestDto);
    }

    @DeleteMapping("/@{username}")
    public UserResponseDto deleteUserByUsername(@PathVariable String username,
                                                @RequestBody CredentialsDto credentialsDto) {
        return userService.deleteUserByUsername(username, credentialsDto);
    }

    @PostMapping("/@{username}/follow")
    public void followUser(@PathVariable String username,
                                      @RequestBody CredentialsDto credentialsDto) {
        userService.followUser(username, credentialsDto);
    }

    @PostMapping("/@{username}/unfollow")
    public void unfollowUser(@PathVariable String username,
                             @RequestBody CredentialsDto credentialsDto) {
        userService.unfollowUser(username, credentialsDto);
    }

    @GetMapping("/@{username}/feed")
    public List<TweetResponseDto> getUserFeed(@PathVariable String username) {
        return userService.getUserFeed(username);
    }

    @GetMapping("/@{username}/tweets")
    public List<TweetResponseDto> getUserTweets(@PathVariable String username) {
        return userService.getUserTweets(username);
    }

    @GetMapping("/@{username}/mentions")
    public List<TweetResponseDto> getUserMentions(@PathVariable String username) {
        return userService.getUserMentions(username);
    }

    @GetMapping("/@{username}/followers")
    public List<UserResponseDto> getUserFollowers(@PathVariable String username) {
        return userService.getUserFollowers(username);
    }

    @GetMapping("/@{username}/following")
    public List<UserResponseDto> getUserFollowing(@PathVariable String username) {
        log.info("Fetching followers for user: {}", username);
        return userService.getUserFollowing(username);
    }

}
