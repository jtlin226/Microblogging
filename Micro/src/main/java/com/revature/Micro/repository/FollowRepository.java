package com.revature.Micro.repository;

import com.revature.Micro.Entity.Follow;
import com.revature.Micro.Entity.Micro;
import com.revature.Micro.Entity.MicroUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Integer> {

    @Query("select f from Follow f where f.followBy = :followBy AND f.following = :following")
    Optional<Follow> findByFollowBy(@Param("followBy") MicroUser followBy, @Param("following") MicroUser following);

}
