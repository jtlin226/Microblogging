package com.revature.Micro.dto;

import com.revature.Micro.Entity.Micro;
import com.revature.Micro.Entity.MicroUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MicroDTO {
    private int id;
    private String content;
    private int user;

    public Micro convertToEntity(MicroUser user) {
        Micro micro = new Micro();

        micro.setContent(this.content);
        micro.setId(this.id);
        micro.setUser(user);

        return micro;
    }
}
