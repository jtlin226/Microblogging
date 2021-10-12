package com.revature.Micro.repository;

import com.revature.Micro.Entity.Micro;
import com.revature.Micro.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MicroRepository extends JpaRepository<Micro, Integer> {
    /**
     * Custom method that gets a list of reviews of the associated user
     * @param user object parameter to search for reviews
     * @return list of reviews
     */
    @Query("select m from Micro m where m.user = :user")
    List<Micro> getMicrosByUser (@Param("user") User user);

}
