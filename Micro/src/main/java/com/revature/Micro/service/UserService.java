package com.revature.Micro.service;

import com.revature.Micro.Entity.MicroUser;
import com.revature.Micro.dto.AuthenticationRequest;
import com.revature.Micro.dto.AuthenticationResponse;
import com.revature.Micro.repository.UserRepository;
import com.revature.Micro.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final MicroUserDetailsService microUserDetailsService;
    private final JwtUtil jwtTokenUtil;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, MicroUserDetailsService microUserDetailsService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.microUserDetailsService = microUserDetailsService;
        this.jwtTokenUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * save a new user to the db
     * @param microUser
     * @return
     */
    public MicroUser saveNewUser(MicroUser microUser) {
        microUser.setPassword(passwordEncoder.encode(microUser.getPassword()));
        return userRepository.save(microUser);
    }

    /**
     * authenticate that the given username and password are in db
     * @param authReq
     * @return jwt for access to other controller endpoints
     */
    public ResponseEntity<?> authenticate(AuthenticationRequest authReq){
        log.info("verifying user");
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authReq.getUsername(), authReq.getPassword())
            );
            final UserDetails userDetails = microUserDetailsService.loadUserByUsername(authReq.getUsername());
            final String jwt = jwtTokenUtil.generateToken(userDetails);
            AuthenticationResponse authResp = new AuthenticationResponse(jwt);
            return ResponseEntity.ok(authResp);
        } catch (Exception e) {
            log.error("Credentials not recognized during authentication", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * used to get a specific user given the user's username. This is used for when user forgets password
     * @param username
     * @return jwt needed to change password. Lasts for 2 min
     */
    public ResponseEntity<AuthenticationResponse> getSpecificUser(String username){
        final UserDetails userDetails = microUserDetailsService.loadUserByUsername(username);
        final String jwt = jwtTokenUtil.generateTempToken(userDetails);
        AuthenticationResponse authResp = new AuthenticationResponse(jwt);
        return ResponseEntity.ok(authResp);
    }

    /**
     * get a specific user by username.
     * @param username
     * @return user object
     */
    public MicroUser getUserByUsername(String username) {
        try {
            return userRepository.findByUsername(username).orElseThrow(RuntimeException::new);
        } catch (RuntimeException e) {
            log.error("Failed to retrieve user based off username",e);
            return null;
        }
    }

    /**
     * get a list of users where their name contains the given name
     * @param name
     * @return
     */
    public List<MicroUser> searchUsersByName(String name){
        String[] names = name.split(" ");
        if(names.length == 2){
            return userRepository.findByFirstNameContainingAndLastNameContaining(names[0], names[1]).orElseThrow(RuntimeException::new);
        } else {
            return userRepository.findByFirstNameContaining(name).orElseThrow(RuntimeException::new);
        }

    }

    /**
     * get a list of users that follow a given user
     * @param microUser
     * @return
     */
    public List<MicroUser> getAllFollowers(MicroUser microUser) {
        return microUser.getFollower();
    }

    /**
     * get a list of users where their username contains the given name
     * @param name
     * @return
     */
    public List<MicroUser> searchUsersByUsername(String name){
        return userRepository.findByUsernameContaining(name).orElseThrow(RuntimeException::new);
    }

    /**
     * used to update a user's password after it is changed.
     * @param microUser
     * @return
     */
    public MicroUser updateUser(MicroUser microUser){
        microUser.setPassword(passwordEncoder.encode(microUser.getPassword()));
        return userRepository.save(microUser);
    }

    /**
     * used to update a user's profile after the about me and image URL are changed
     * @param user
     * @return
     */
    public MicroUser updateProfile(MicroUser user){
        return userRepository.save(user);
    }

    /**
     * used to update a user's follower list by adding a new user to it
     * @param user
     * @param id
     * @return
     */
    public MicroUser followUser (MicroUser user, int id) {
        user.getFollowing().add(userRepository.findById(id).orElseThrow(RuntimeException::new));
        return userRepository.save(user);
    }

    /**
     * used to update a user's follower list by removing a user from it
     * @param user
     * @param id
     * @return
     */
    public MicroUser unfollowUser (MicroUser user, int id) {
        user.getFollowing().remove(userRepository.findById(id).orElseThrow(RuntimeException::new));
        return userRepository.save(user);
    }
}
