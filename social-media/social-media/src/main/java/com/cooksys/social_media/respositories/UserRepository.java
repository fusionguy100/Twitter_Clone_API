package com.cooksys.social_media.respositories;

import com.cooksys.social_media.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCredentials_Username(String normalized);

    List<User> findAllByDeletedIsFalse();

    User findByCredentialsUsernameAndCredentialsPassword(String username, String password);

    boolean existsByCredentialsUsernameAndDeletedIsFalse(String username);

    boolean existsByCredentialsUsernameAndDeletedIsTrue(String username);

    User findByCredentialsUsernameAndDeletedIsFalse(String username);

    Optional <User> findByCredentialsUsernameAndCredentialsPasswordAndDeletedIsFalse(String username, String password);
}
