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

    public MicroUser saveNewUser(MicroUser microUser) {
        microUser.setPassword(passwordEncoder.encode(microUser.getPassword()));
        return userRepository.save(microUser);
    }

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

    public MicroUser getSpecificUser(String username){
        return userRepository.findByUsername(username).orElseThrow(RuntimeException::new);
    }

    public MicroUser getUserByUsername(String username) {
        try {
            return userRepository.findByUsername(username).orElseThrow(RuntimeException::new);
        } catch (RuntimeException e) {
            log.error("Failed to retrieve user based off username",e);
            return null;
        }
    }

    public List<MicroUser> searchUsersByName(String name){
        String[] names = name.split("_");
        if(names.length == 2){
            return userRepository.findByFirstNameContainingAndLastNameContaining(names[0], names[1]).orElseThrow(RuntimeException::new);
        } else {
            return userRepository.findByFirstNameContaining(name).orElseThrow(RuntimeException::new);
        }

    }

    public List<MicroUser> getAllFollowers(MicroUser microUser) {
        return microUser.getFollower();
    }
    public List<MicroUser> searchUsersByUsername(String name){
        return userRepository.findByUsernameContaining(name).orElseThrow(RuntimeException::new);
    }

    public MicroUser updateUser(MicroUser microUser){
        microUser.setPassword(passwordEncoder.encode(microUser.getPassword()));
        return userRepository.save(microUser);
    }

    public MicroUser followUser (MicroUser user, int id) {
        user.getFollowing().add(userRepository.findById(id).orElseThrow(RuntimeException::new));
        return userRepository.save(user);
    }
    public MicroUser unfollowUser (MicroUser user, int id) {
        user.getFollowing().remove(userRepository.getById(id));
        return userRepository.save(user);
    }
}
