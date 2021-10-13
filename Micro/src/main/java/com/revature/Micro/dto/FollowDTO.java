package com.revature.Micro.dto;

import com.revature.Micro.Entity.Follow;
import com.revature.Micro.Entity.MicroUser;

public class FollowDTO {
    private int followBy;
    private int following;

    public Follow convertToEntity (MicroUser followBy, MicroUser following) {
        Follow follow = new Follow();
        follow.setFollowBy(followBy);
        follow.setFollowing(following);
        return follow;
    }
    /**
     * SELECT User.username, Follow.following FROM User JOIN Follow ON User.id = Follow.followBy
     */
}
