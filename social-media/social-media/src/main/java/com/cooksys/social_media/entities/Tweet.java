package com.cooksys.social_media.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude={"author", "likedBy", "mentions", "inReplyTo", "replies", "repostOf", "reposts", "hashtags"})
@ToString(exclude={"author", "likedBy", "mentions", "inReplyTo", "replies", "repostOf", "reposts", "hashtags"})
public class Tweet {
    @Id
    @GeneratedValue
    private long id;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp posted;

    private String content;
    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToMany(mappedBy = "likedTweets")
    private Set<User> likedBy;

    @ManyToMany(mappedBy = "mentionedIn")
    private Set<User> mentions;

    @ManyToOne
    @JoinColumn(name = "inReplyTo")
    private Tweet inReplyTo;

    @OneToMany(mappedBy = "inReplyTo")
    private Set<Tweet> replies;

    @ManyToOne
    @JoinColumn(name="repostOf")
    private Tweet repostOf;

    @OneToMany(mappedBy = "repostOf")
    private Set<Tweet> reposts;

    @ManyToMany
    @JoinTable(
        name = "tweet_hashtags",
        joinColumns = @JoinColumn(name = "tweet_id"),
        inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private Set<Hashtag> hashtags;
}
