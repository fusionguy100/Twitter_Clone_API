package com.cooksys.social_media.services.impl;

import com.cooksys.social_media.entities.User;
import com.cooksys.social_media.mappers.HashtagMapper;
import com.cooksys.social_media.respositories.HashTagRepository;
import com.cooksys.social_media.respositories.UserRepository;
import com.cooksys.social_media.services.ValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {

    private final HashTagRepository hashTagRepository;
    private final HashtagMapper hashtagMapper;

    private final UserRepository userRepository;

    @Override
    public boolean validateTagExists(String label) {
        String normalized = label == null ? "" : label.trim().replaceFirst("^#", "").toLowerCase();
        return hashTagRepository.existsByLabelIgnoreCase(normalized);
    }


    @Override
    public boolean validateUsernameExists(String username) {
        String normalized = username.trim().toLowerCase();
        return userRepository.findByCredentials_Username(normalized).isPresent();
    }

    @Override
    public boolean validateUsernameAvailable(String username) {
        String normalized = username.trim().toLowerCase();
        Optional<User> optionalUser = userRepository.findByCredentials_Username(normalized); //get the user
        if(optionalUser.isEmpty()) {
            return true;
        } else {
            User user = optionalUser.get();
            return user.isDeleted();
        }
    }
}
