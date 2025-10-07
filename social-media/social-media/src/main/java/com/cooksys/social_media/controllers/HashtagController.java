package com.cooksys.social_media.controllers;

import com.cooksys.social_media.dtos.HashtagDto;
import com.cooksys.social_media.dtos.TweetResponseDto;
import com.cooksys.social_media.services.HashtagService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/tags")
public class HashtagController {

    private final HashtagService hashtagService;

    @GetMapping
    public List<HashtagDto> getAllTags() {
        return hashtagService.getAllTags();
    }

    @GetMapping("/{label}")
    public List<TweetResponseDto> getTag(@PathVariable String label) {
        return hashtagService.getTag(label);
    }

}
