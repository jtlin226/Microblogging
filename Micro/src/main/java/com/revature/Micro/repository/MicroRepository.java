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

}
