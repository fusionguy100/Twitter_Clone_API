package com.cooksys.social_media.controllers;

import com.cooksys.social_media.dtos.*;
import com.cooksys.social_media.services.TweetService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/tweets")
public class TweetController {

    private final TweetService tweetService;
    @GetMapping
    public List<TweetResponseDto> getTweets() {
        return tweetService.getTweets();
    }

    @PostMapping
    public TweetResponseDto postTweet(@RequestBody TweetRequestDto tweetRequestDto) {
        return tweetService.postTweet(tweetRequestDto);
    }

    @GetMapping("/{id}")
    public TweetResponseDto getTweetById(@PathVariable Long id) {
        return tweetService.getTweetById(id);
    }

    @DeleteMapping("/{id}")
    public TweetResponseDto deleteTweetById(@PathVariable Long id,
                                            @RequestBody CredentialsDto credentialsDto) {
        return tweetService.deleteTweetById(id, credentialsDto);
    }

    @PostMapping("/{id}/like")
    public void likeTweetById(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
        tweetService.likeTweetById(id, credentialsDto);
    }

    @PostMapping("/{id}/reply")
    public TweetResponseDto replyToTweetById(@PathVariable Long id,
                                   @RequestBody TweetRequestDto tweetRequestDto) {
        return tweetService.replyToTweetById(id, tweetRequestDto);
    }

    @PostMapping("/{id}/repost")
    public TweetResponseDto repostTweetById(@PathVariable Long id,
                                  @RequestBody CredentialsDto credentialsDto) {
        return tweetService.repostTweetById(id, credentialsDto);
    }

    @GetMapping("/{id}/tags")
    public List<HashtagDto> getTagsByTweetId(@PathVariable Long id) {
        return tweetService.getTagsByTweetId(id);
    }

    @GetMapping("/{id}/likes")
    public List<UserResponseDto> getLikesByTweetId(@PathVariable Long id) {
        return tweetService.getLikesByTweetId(id);
    }

    @GetMapping("/{id}/context")
    public ContextDto getContextByTweetId(@PathVariable Long id) {
        return tweetService.getContextByTweetId(id);
    }

    @GetMapping("/{id}/replies")
    public List<TweetResponseDto> getRepliesByTweetId(@PathVariable Long id) {
        return tweetService.getRepliesByTweetId(id);
    }

    @GetMapping("/{id}/reposts")
    public List<TweetResponseDto> getRepostsByTweetId(@PathVariable Long id) {
        return tweetService.getRepostsByTweetId(id);
    }

    @GetMapping("/{id}/mentions")
    public List<UserResponseDto> getMentionsByTweetId(@PathVariable Long id) {
        return tweetService.getMentionsByTweetId(id);
    }
}
