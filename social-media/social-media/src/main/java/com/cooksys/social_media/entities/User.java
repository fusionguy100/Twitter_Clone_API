package com.cooksys.social_media.entities;

import com.cooksys.social_media.entities.embeddables.Credentials;
import com.cooksys.social_media.entities.embeddables.Profile;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Table(name="user_table")
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"followers", "following"})
@ToString(exclude = {"followers", "following"})
public class User {

    @Id
    @GeneratedValue
    private Long id;
    @Embedded
    Profile profile;
    @Embedded
    Credentials credentials;
    private boolean deleted = false;
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp joined;

    @ManyToMany //user THIS user follows, owning side; writes followers_following roles)
    @JoinTable(
            name="followers_following",
            joinColumns = @JoinColumn(name="follower_id"), // this users id
            inverseJoinColumns = @JoinColumn(name="following_id") // others id
    )
    private Set<User> following;

    @ManyToMany(mappedBy = "following") // users who follow this user
    private Set<User> followers;

    @OneToMany(mappedBy = "author")
    private List<Tweet> tweets;

    @ManyToMany
    @JoinTable(
            name="user_likes",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="tweet_id")
    )
    private Set<Tweet> likedTweets;

    @ManyToMany
    @JoinTable(
            name="user_mentions",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="tweet_id")
    )
    private Set<Tweet> mentionedIn;
}
