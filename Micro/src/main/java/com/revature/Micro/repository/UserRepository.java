package com.revature.Micro.repository;

import com.revature.Micro.Entity.MicroUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<MicroUser, Integer> {
    Optional<MicroUser> findByUsername(String s);

    Optional<List<MicroUser>> findByFirstNameContainingAndLastNameContaining(String firstName, String lastName);
    Optional<List<MicroUser>> findByFirstNameContaining(String firstName);
    Optional<List<MicroUser>> findByUsernameContaining(String username);

}
