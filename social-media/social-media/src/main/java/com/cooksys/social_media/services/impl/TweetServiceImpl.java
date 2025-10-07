package com.cooksys.social_media.services.impl;

import com.cooksys.social_media.dtos.*;
import com.cooksys.social_media.entities.Hashtag;
import com.cooksys.social_media.entities.Tweet;
import com.cooksys.social_media.entities.User;
import com.cooksys.social_media.exceptions.BadRequestException;
import com.cooksys.social_media.exceptions.NotAuthorizedException;
import com.cooksys.social_media.exceptions.NotFoundException;
import com.cooksys.social_media.mappers.HashtagMapper;
import com.cooksys.social_media.mappers.TweetMapper;
import com.cooksys.social_media.mappers.UserMapper;
import com.cooksys.social_media.respositories.HashTagRepository;
import com.cooksys.social_media.respositories.TweetRepository;
import com.cooksys.social_media.respositories.UserRepository;
import com.cooksys.social_media.services.TweetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final HashTagRepository hashTagRepository;
    private final TweetMapper tweetMapper;
    private final UserMapper userMapper;
    private final HashtagMapper hashtagMapper;

    @Override
    public List<TweetResponseDto> getTweets() {
        return tweetMapper.entitiesToDtos(tweetRepository.findAllByDeletedFalse());
    }

    @Override
    public TweetResponseDto postTweet(TweetRequestDto tweetRequestDto) {
        if (tweetRequestDto.getCredentials() == null
                || tweetRequestDto.getCredentials().getUsername() == null
                || tweetRequestDto.getCredentials().getPassword() == null) {
            throw new NotAuthorizedException("Credentials required.");
        }
        String username = tweetRequestDto.getCredentials().getUsername().trim().toLowerCase();
        String password = tweetRequestDto.getCredentials().getPassword();
        User user = userRepository.findByCredentialsUsernameAndCredentialsPassword(username,password);
        if (user == null) {
            throw new NotAuthorizedException("Invalid credentials");
        }
        if (!userRepository.existsByCredentialsUsernameAndDeletedIsFalse(user.getCredentials().getUsername())) {
            throw new NotFoundException("User not found");
        }
        String content = (tweetRequestDto.getContent() == null)? "" : tweetRequestDto.getContent().trim();
        if(content.isEmpty()) {
            throw new BadRequestException("Tweet must include non empty content!");
        }

        Tweet tweet = new Tweet();
        tweet.setAuthor(user);
        tweet.setContent(content);
        tweet.setPosted(new Timestamp(System.currentTimeMillis()));
        tweet = tweetRepository.saveAndFlush(tweet);

        if (user.getTweets() != null) {
            user.getTweets().add(tweet);
            userRepository.saveAndFlush(user);
        }


        for (String mentionedUsername : extractMentions(content)) {
            User mentioned = userRepository.findByCredentialsUsernameAndDeletedIsFalse(mentionedUsername);
            if (mentioned != null) {
                mentioned.getMentionedIn().add(tweet);
                userRepository.save(mentioned);
            }
        }

       // Process hashtags
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (tweet.getHashtags() == null) {
            tweet.setHashtags(new java.util.HashSet<>());
        }
        for (String label : extractHashtags(content)) {
            Hashtag tag = hashTagRepository.findByLabelIgnoreCase(label)
                    .orElseGet(() -> {
                        Hashtag hashtag = new Hashtag();
                        hashtag.setLabel(label); // normalize to lowercase
                        hashtag.setFirstUsed(now);
                        hashtag.setLastUsed(now);
                        return hashtag;
                    });

            tag.setLastUsed(now);
            tag = hashTagRepository.save(tag);

            if (tweet.getHashtags() == null) {
                tweet.setHashtags(new HashSet<>());
            }
            tweet.getHashtags().add(tag);
        }
        tweet = tweetRepository.saveAndFlush(tweet);

        return tweetMapper.entityToDto(tweet);
    }

    //helper methods
    private Set<String> extractMentions(String text) {
        final Pattern MENTION_PATTERN = Pattern.compile("@(\\w+)");


        Set<String> mentions = new HashSet<>();
        Matcher matcher = MENTION_PATTERN.matcher(text);
        while (matcher.find()) {
            mentions.add(matcher.group(1));
        }
        return mentions;
    }

    private Set<String> extractHashtags(String text) {
        final Pattern HASHTAG_PATTERN = Pattern.compile("#(\\w+)");
        Set<String> hashtags = new HashSet<>();
        Matcher matcher = HASHTAG_PATTERN.matcher(text);
        while (matcher.find()) {
//            hashtags.add(matcher.group(1).toLowerCase()); // normalize label
            hashtags.add(matcher.group(1));
        }
        return hashtags;
    }


    @Override
    public TweetResponseDto getTweetById(Long id) {
       Tweet tweet = tweetRepository.findById(id).orElseThrow(()-> new NotFoundException("Tweet not found"));
       if(tweet.isDeleted()) throw new NotFoundException("Tweet not found");
       return tweetMapper.entityToDto(tweet);
    }

    @Override
    public TweetResponseDto deleteTweetById(Long id, CredentialsDto credentialsDto) {
        User user = userRepository.findByCredentialsUsernameAndCredentialsPassword(
                credentialsDto.getUsername(), credentialsDto.getPassword());

        if (user == null) throw new NotAuthorizedException("Invalid credentials");
        if (!userRepository.existsByCredentialsUsernameAndDeletedIsFalse(user.getCredentials().getUsername()))
            throw new NotFoundException("User not found");

        Tweet tweet = tweetRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new NotFoundException("Tweet not found"));

        if (!tweet.getAuthor().equals(user))
            throw new NotAuthorizedException("You are not the author of this tweet!");


        TweetResponseDto response = tweetMapper.entityToDto(tweet);

        tweet.setDeleted(true);
        tweetRepository.saveAndFlush(tweet);

        return response;
    }

    @Override
    public void likeTweetById(Long id, CredentialsDto credentialsDto) {
        if (credentialsDto == null ||
                credentialsDto.getUsername() == null ||
                credentialsDto.getPassword() == null) {
            throw new NotAuthorizedException("Credentials required.");
        }

        User user = userRepository.findByCredentialsUsernameAndCredentialsPassword(
                credentialsDto.getUsername().trim().toLowerCase(),
                credentialsDto.getPassword()
        );
        if (user == null || user.isDeleted()) {
            throw new NotAuthorizedException("Invalid credentials or inactive user.");
        }


        Tweet tweet = tweetRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new NotFoundException("Tweet not found"));

        Tweet anotherTweet = tweetRepository.findById(id).filter(t-> !t.isDeleted()).orElseThrow(()-> new NotFoundException( "Tweet is not found!"));


        if (user.getLikedTweets() == null) {
            user.setLikedTweets(new java.util.HashSet<>());
        }

        boolean alreadyLiked = user.getLikedTweets().stream()
                .anyMatch(t -> t.getId() == tweet.getId());
        if (!alreadyLiked) {
            user.getLikedTweets().add(tweet);
            userRepository.saveAndFlush(user);
        }


    }

    @Override
    public TweetResponseDto replyToTweetById(Long id, TweetRequestDto tweetRequestDto) {
        if (tweetRequestDto.getCredentials() == null
                || tweetRequestDto.getCredentials().getUsername() == null
                || tweetRequestDto.getCredentials().getPassword() == null) {
            throw new NotAuthorizedException("Credentials required.");
        }
        String username = tweetRequestDto.getCredentials().getUsername().trim().toLowerCase();
        String password = tweetRequestDto.getCredentials().getPassword();
        User user = userRepository.findByCredentialsUsernameAndCredentialsPassword(username,password);
        if (user == null) {
            throw new NotAuthorizedException("Invalid credentials");
        }
        if (!userRepository.existsByCredentialsUsernameAndDeletedIsFalse(user.getCredentials().getUsername())) {
            throw new NotFoundException("User not found");
        }
        Tweet tweet = tweetRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new NotFoundException("Tweet not found"));

        String content = (tweetRequestDto.getContent() == null)? "" : tweetRequestDto.getContent().trim();
        if(content.isEmpty()) {
            throw new BadRequestException("Tweet must include non empty content!");
        }
        Tweet reply = new Tweet();
        reply.setAuthor(user);
        reply.setContent(content);
        reply.setPosted(new Timestamp(System.currentTimeMillis()));
        reply.setInReplyTo(tweet); //  server sets the relationship
        reply = tweetRepository.saveAndFlush(reply);

        if (user.getTweets() != null) {
            user.getTweets().add(reply);
            userRepository.saveAndFlush(user);
        }

       // Process @mentions
        for (String mentionedUsername : extractMentions(content)) {
            // only mention active users
            User mentioned = userRepository.findByCredentialsUsernameAndDeletedIsFalse(mentionedUsername);
            if (mentioned != null) {
                if (mentioned.getMentionedIn() == null) {
                    mentioned.setMentionedIn(new java.util.HashSet<>());
                }
                mentioned.getMentionedIn().add(reply);
                userRepository.save(mentioned);
            }
        }
        // Process #hashtags
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (reply.getHashtags() == null) {
            reply.setHashtags(new java.util.HashSet<>());
        }
        for (String label : extractHashtags(content)) {
            Hashtag tag = hashTagRepository.findByLabelIgnoreCase(label)
                    .orElseGet(() -> {
                        Hashtag h = new Hashtag();
                        h.setLabel(label.toLowerCase()); // normalize label
                        h.setFirstUsed(now);
                        h.setLastUsed(now);
                        return h;
                    });
            tag.setLastUsed(now);
            tag = hashTagRepository.save(tag);

            reply.getHashtags().add(tag);
        }
        reply = tweetRepository.saveAndFlush(reply);
        return tweetMapper.entityToDto(reply);
    }

    @Override
    public List<HashtagDto> getTagsByTweetId(Long id) {
        Tweet tweet = tweetRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new NotFoundException("Tweet not found"));
        Set<Hashtag> tags = tweet.getHashtags();
        if(tags == null || tags.isEmpty()) {
            return List.of();
        }
        return hashtagMapper.entitiesToDtos(tags.stream().toList());

    }

    @Override
    public ContextDto getContextByTweetId(Long id) {
        Tweet tweet = tweetRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new NotFoundException("Tweet not found"));
        // before chain
        List<TweetResponseDto> before = new ArrayList<>();
        Tweet current = tweet.getInReplyTo();
        while (current != null && !current.isDeleted()) {
            before.add(0, tweetMapper.entityToDto(current));
            current = current.getInReplyTo();
        }

        // after chain
        List<TweetResponseDto> after = new ArrayList<>();
        collectReplies(tweet, after);
        after.sort(Comparator.comparing(TweetResponseDto::getPosted));

        // build response
        ContextDto contextDto = new ContextDto();
        contextDto.setTarget(tweetMapper.entityToDto(tweet));
        contextDto.setBefore(before);
        contextDto.setAfter(after);

        return contextDto;

    }

    //helper method
    private void collectReplies(Tweet root, List<TweetResponseDto> out) {
        for (Tweet reply : root.getReplies()) {
            if (!reply.isDeleted()) {
                out.add(tweetMapper.entityToDto(reply));
                collectReplies(reply, out);
            }
        }
    }

    @Override
    public List<UserResponseDto> getLikesByTweetId(Long id) {
        Tweet tweet = tweetRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new NotFoundException("Tweet not found"));
        List<UserResponseDto> activeUsers = new ArrayList<>();
        for (User user: tweet.getLikedBy()) {
             if(!user.isDeleted()) {
                 activeUsers.add(userMapper.entityToDto(user));
             }
        }
        return activeUsers;
    }

    @Override
    public List<TweetResponseDto> getRepliesByTweetId(Long id) {
        Tweet tweet = tweetRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new NotFoundException("Tweet not found"));

        List<TweetResponseDto> directReplies = new ArrayList<>();
        for(Tweet t: tweet.getReplies()) {
            if(!t.isDeleted()) {
                directReplies.add(tweetMapper.entityToDto(t));
            }
        }
        return directReplies;
    }

    @Override
    public List<TweetResponseDto> getRepostsByTweetId(Long id) {
        Tweet tweet = tweetRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new NotFoundException("Tweet not found"));
        List<TweetResponseDto> reposts = new ArrayList<>();
        for(Tweet t: tweet.getReposts()) {
            if(!t.isDeleted()) {
                reposts.add(tweetMapper.entityToDto(t));
            }
        }

        return reposts;

    }

    @Override
    public List<UserResponseDto> getMentionsByTweetId(Long id) {
        Tweet tweet = tweetRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new NotFoundException("Tweet not found"));
        Set<User> mentions = tweet.getMentions();
        if (mentions == null || mentions.isEmpty()) {
            return List.of();
        }
        List<UserResponseDto> activeMentions = new ArrayList<>();
        for(User user: mentions) {
            if(!user.isDeleted()) {
                activeMentions.add(userMapper.entityToDto(user));
            }
        }
        return activeMentions;
    }

    @Override
    public TweetResponseDto repostTweetById(Long id, CredentialsDto credentialsDto) {
        if (credentialsDto == null
                || credentialsDto.getUsername() == null
                || credentialsDto.getPassword() == null) {
            throw new NotAuthorizedException("Credentials required.");
        }

        User user = userRepository.findByCredentialsUsernameAndCredentialsPassword(credentialsDto.getUsername(),
                credentialsDto.getPassword());

        if (user == null) {
            throw new NotAuthorizedException("Invalid credentials");
        }
        if (!userRepository.existsByCredentialsUsernameAndDeletedIsFalse(user.getCredentials().getUsername())) {
            throw new NotFoundException("User not found");
        }
        Tweet original = tweetRepository.findById(id)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new NotFoundException("Tweet not found"));

        Tweet repost = new Tweet();

        repost.setAuthor(user);
        repost.setPosted(new Timestamp(System.currentTimeMillis()));
        repost.setRepostOf(original);
        repost.setContent(null);

        repost = tweetRepository.saveAndFlush(repost);
        if(user.getTweets() != null) {
            user.getTweets().add(repost);
            userRepository.saveAndFlush(user);
        }
        return tweetMapper.entityToDto(repost);


    }
}
