package com.cooksys.social_media.services.impl;

import com.cooksys.social_media.dtos.HashtagDto;
import com.cooksys.social_media.dtos.TweetResponseDto;
import com.cooksys.social_media.entities.Hashtag;
import com.cooksys.social_media.exceptions.NotFoundException;
import com.cooksys.social_media.mappers.HashtagMapper;
import com.cooksys.social_media.mappers.TweetMapper;
import com.cooksys.social_media.respositories.HashTagRepository;
import com.cooksys.social_media.respositories.TweetRepository;
import com.cooksys.social_media.services.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashTagRepository hashTagRepository;
    private final TweetRepository tweetRepository;
    private final HashtagMapper hashtagMapper;
    private final TweetMapper tweetMapper;

    @Override
    public List<HashtagDto> getAllTags() {
        return hashtagMapper.entitiesToDtos(hashTagRepository.findAll());
    }

    @Override
    public List<TweetResponseDto> getTag(String label) {
//        if (!hashTagRepository.existsByLabel(label)) {
//            throw new NotFoundException("Hashtag with label " + label + " not found");
//        }

        Optional<Hashtag> optionalHashtag = hashTagRepository.findByLabelIgnoreCase(label);
        if (optionalHashtag.isEmpty()) {
            throw new NotFoundException("Hashtag with label " + label + " not found");
        }
        Hashtag hashtag = optionalHashtag.get();
        return tweetMapper.entitiesToDtos(hashtag.getTweets().stream()
                .filter(tweet -> !tweet.isDeleted())
                .sorted((t1, t2) -> t2.getPosted().compareTo(t1.getPosted()))
                .toList());


    }
}
