package com.revature.Micro.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The content of the micro post
     */
    @Column(name = "content")
    private String content;

    /**
     * The user that created this micro, foreign key referencing the User object
     */
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private MicroUser user;

}
