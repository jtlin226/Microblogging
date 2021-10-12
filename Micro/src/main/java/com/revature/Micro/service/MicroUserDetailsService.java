package com.revature.Micro.service;

import com.revature.Micro.Entity.MicroUser;
import com.revature.Micro.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Service layer for RevUserDetails
 */
@Service
public class MicroUserDetailsService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(MicroUserDetailsService.class);
    /**
     * Repository layer for RevUser object
     */
    @Autowired
    private final UserRepository userRepository;

    /**
     * Constructor for RevUserDetailsService
     * @param userRepository UserRepository object
     */
    @Autowired
    public MicroUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Overridden UserDetails for Spring Security
     * @param s username of RevUser
     * @return UserDetails object for RevUser
     * @throws UsernameNotFoundException To indicate invalid log in credentials
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.info("Load user information");
        MicroUser microUser = userRepository.findByUsername(s).orElseThrow(RuntimeException::new);
        String username = microUser.getUsername();
        String password = microUser.getPassword();
        return new User(username, password, new ArrayList<>()); // ArrayList because we aren't dealing with Authorities
    }



}