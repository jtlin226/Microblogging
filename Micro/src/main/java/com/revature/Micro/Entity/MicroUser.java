package com.revature.Micro.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="user")

@Getter
@Setter
@NoArgsConstructor

public class MicroUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable=false)
    private String firstName;

    @Column(name = "last_name", nullable=false)
    private String lastName;

    @Column(name = "image")
    private String imageURL;

    @Column(name = "about")
    private String about;
//
//    @ManyToMany(mappedBy = "following")
//    @JsonIgnoreProperties("follower")
//    private List<MicroUser> following;
//
//    @ManyToMany
//    @JoinTable(
//            name="followedBy",
//            joinColumns=@JoinColumn(name = "follower_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name="following_id", referencedColumnName = "id")
//    )
//    @JsonIgnoreProperties("following")
//    private List<MicroUser> follower;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private List<Micro> micros;
}
