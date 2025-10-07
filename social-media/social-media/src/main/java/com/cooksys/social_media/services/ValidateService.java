package com.cooksys.social_media.services;

public interface ValidateService {

    boolean validateTagExists(String label);

    boolean validateUsernameExists(String username);

    boolean validateUsernameAvailable(String username);
}
