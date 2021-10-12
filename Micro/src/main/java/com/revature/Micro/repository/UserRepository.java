package com.revature.Micro.repository;

import com.revature.Micro.Entity.MicroUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<MicroUser, Integer> {
    Optional<MicroUser> findByUsername(String s);
}
