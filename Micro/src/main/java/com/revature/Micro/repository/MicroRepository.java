package com.revature.Micro.repository;

import com.revature.Micro.Entity.Micro;
import com.revature.Micro.Entity.MicroUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MicroRepository extends JpaRepository<Micro, Integer> {
//
//    @Query("select m from Micro m where m.user = :user")
//    List<Micro> getMicrosByUser (@Param("user") MicroUser user);

    List<Micro> findByUser(MicroUser user);

    @Query("SELECT m FROM Micro m WHERE m.user = :user OR m.user IN (SELECT following_id FROM followed_by WHERE followed_by.follower_id = :user)")
    List<Micro> getAllMicrosFromUserAndFollowingByUser (@Param("user") MicroUser user);
    /**
     SELECT * FROM MICROS
     WHERE USER_ID = 2 OR USER_ID IN(
     SELECT FOLLOWING_ID FROM FOLLOWED_BY WHERE FOLLOWED_BY.FOLLOWER_ID = 2)
     */
}
