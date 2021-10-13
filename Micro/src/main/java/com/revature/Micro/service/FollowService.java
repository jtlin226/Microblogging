package com.revature.Micro.service;

import com.revature.Micro.Entity.Follow;
import com.revature.Micro.Entity.Micro;
import com.revature.Micro.Entity.MicroUser;
import com.revature.Micro.repository.FollowRepository;
import com.revature.Micro.repository.MicroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowService {

    private final FollowRepository followRepository;

    @Autowired
    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    public void deleteFollow (MicroUser followBy, MicroUser following){
        followRepository.findByFollowBy(followBy, following).ifPresent(followRepository::delete);
    }

    public Follow saveFollow(MicroUser followBy, MicroUser following) {
        Follow follow = new Follow();
        follow.setFollowBy(followBy);
        follow.setFollowing(following);
        return followRepository.save(follow);
    }

    public Follow getFollowByFollowBy(MicroUser followBy, MicroUser following) {
        return followRepository.findByFollowBy(followBy, following).orElseThrow(RuntimeException::new);
    }

    public List<Follow> getAll() {
        return followRepository.findAll();
    }
}
