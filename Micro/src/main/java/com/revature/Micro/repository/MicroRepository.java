package com.revature.Micro.repository;

import com.revature.Micro.Entity.Micro;
import com.revature.Micro.Entity.MicroUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MicroRepository extends JpaRepository<Micro, Integer> {

    @Query("select m from Micro m where m.user = :user")
    List<Micro> getMicrosByUser (@Param("user") MicroUser user);

    @Query("SELECT m FROM Micro m JOIN MicroUser u ON m.user = u.id WHERE m.user = :user")
    List<Micro> getAllMicrosFromUserAndFollowingByUser (@Param("user") MicroUser user);
}
