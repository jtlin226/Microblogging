package com.revature.Micro.dto;

import com.revature.Micro.Entity.MicroUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MicroUserDTO {
    private int id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String imageURL;
    private String about;

    private List<Integer> following;
    private List<Integer> follower;

    private List<Integer> micros;

    public MicroUser convertToEntity(){

        MicroUser user = new MicroUser();

        return user;
    }
}
