package com.cooksys.social_media.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Hashtag {
    @Id
    @GeneratedValue
    private Long id;

    private String label;
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp firstUsed;
    @UpdateTimestamp
    private Timestamp lastUsed;

    @ManyToMany(mappedBy = "hashtags")
    private Set<Tweet> tweets;
}
