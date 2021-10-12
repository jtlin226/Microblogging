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

    public MicroUser getUserByUsername(String username) {
        try {
            return userRepository.findByUsername(username).orElseThrow(RuntimeException::new);
        } catch (RuntimeException e) {
            log.error("Failed to retrieve user based off username",e);
            return null;
        }
    }
}
