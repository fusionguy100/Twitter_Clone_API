package com.cooksys.social_media.respositories;

import com.cooksys.social_media.entities.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HashTagRepository extends JpaRepository<Hashtag, Long> {

    Optional<Hashtag> findByLabelIgnoreCase(String name);

    Hashtag findByLabel(String label);

    boolean existsByLabel(String label);
    boolean existsByLabelIgnoreCase(String label);
}
