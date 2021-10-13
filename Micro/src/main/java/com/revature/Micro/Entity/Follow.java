package com.revature.Micro.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="follow")
@Getter
@Setter
@NoArgsConstructor

public class Follow {

    @Id
    @ManyToOne
    @JoinColumn(name = "followBy")
    private MicroUser followBy;

    @Id
    @ManyToOne
    @JoinColumn(name = "following")
    private MicroUser following;
}
