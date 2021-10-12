package com.revature.Micro.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Review object model that contains user rating and
 * micro content of a particular Ghibli Studio film
 * and the user that created this micro
 */
@Entity
@Table(name = "micros")
@Getter
@Setter
@NoArgsConstructor
public class Micro {
    /**
     * Unique primary key with auto increment value for micros table
     */
    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int microId;

    /**
     * The content of the micro post
     */
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    /**
     * The user that created this micro, foreign key referencing the User object
     */
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
