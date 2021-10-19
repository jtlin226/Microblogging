package com.revature.Micro.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.Micro.Entity.MicroUser;
import com.revature.Micro.dto.AuthenticationRequest;
import com.revature.Micro.service.UserService;
import com.revature.Micro.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;


    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    /**
     * registering a new user. Calls saveNewUser method in UserService
     * @param microUser user object that is to be persisted
     * @return the user that was persisted
     */
    @PostMapping("/register")
    public MicroUser createUser(@RequestBody MicroUser microUser){
        log.info("New user being created");
        return userService.saveNewUser(microUser);
    }

    /**
     * authenticating a user when they log in. Calls authenticate method in UserService
     * @param authReq object with username and password needed to authenticate
     * @return the jwt generated after authentication
     */
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticateToken(@RequestBody AuthenticationRequest authReq){
        log.info("User attempting to login.");
        return userService.authenticate(authReq);
    }

    /**
     * get user based off the incoming jwt
     * @return
     */
    @GetMapping
    public ResponseEntity<String> getCurrentUser(){
        try{
            return ResponseEntity.ok().body(
                    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
                            JwtUtil.extractUser(userService)
                    )
            );
        } catch (Exception e) {
            log.error("Failed to write");
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * get user object given a username, used for when user forgot password
     * @param username string of username
     * @return
     */
    @GetMapping("/recover/{username}")
    public ResponseEntity<?> getSpecificUser(@PathVariable String username){
        return userService.getSpecificUser(username);
    }

    /**
     * get all users where name contains the given name
     * @param name The name of the person that is being searched. Format: firstName  or   firstName_lastName
     * @return
     */
    @GetMapping("/search/{name}")
    public ResponseEntity<String> searchPeopleByName(@PathVariable String name){
        try{
            return ResponseEntity.ok().body(
                    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
                            userService.searchUsersByName(name)
                    )
            );
        } catch(RuntimeException e){
            log.warn("Failed to find users", e);
            try{
                return ResponseEntity.ok(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(new ArrayList<>()));
            } catch(JsonProcessingException ex){
                log.error("Failed to write empty arraylist",e);
                return ResponseEntity.internalServerError().build();
            }

        } catch (Exception e){
            log.warn("Failed to search users", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * get all users that follow current user
     * @return
     */
    @GetMapping("/follower")
    public ResponseEntity<String> getMyFollowers() {
        MicroUser microUser = JwtUtil.extractUser(userService);

        try {
            return ResponseEntity.ok(new ObjectMapper().
                    writerWithDefaultPrettyPrinter().
                    writeValueAsString(userService.getAllFollowers(microUser)));
        } catch (Exception e) {
            log.error("Failed to update user.", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * get all users where username contains given username string
     * @param username string of username
     * @return
     */
    @GetMapping("/{username}")
    public ResponseEntity<String> searchPeopleByUsername(@PathVariable String username){
        try{
            return ResponseEntity.ok().body(
                    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
                            userService.searchUsersByUsername(username)
                    )
            );
        } catch(RuntimeException e){
            log.warn("Failed to find users", e);
            try{
                return ResponseEntity.ok(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(new ArrayList<>()));
            } catch(JsonProcessingException ex){
                log.error("Failed to write empty arraylist",e);
                return ResponseEntity.internalServerError().build();
            }

        } catch (Exception e){
            log.warn("Failed to search users", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * get all users that current user is following
     * @return
     */
    @GetMapping("/following")
    public ResponseEntity<String> getFollowing(){
        try{
            return ResponseEntity.ok().body(
                    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
                            JwtUtil.extractUser(userService).getFollowing()
                    )
            );
        } catch (Exception e){
            log.error("Failed to write followers.", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * update user object with new password
     * @param microUser user object that contains the information that needs to be persisted to the current user
     * @return
     */
    @PutMapping("/password")
    public ResponseEntity<String> updateUser(@RequestBody MicroUser microUser){
        MicroUser user = JwtUtil.extractUser(userService);
        user.setPassword(microUser.getPassword());
        try{
            return ResponseEntity.ok().body(
                    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
                            userService.updateUser(user)
                    )
            );
        } catch (Exception e){
            log.error("Failed to update user.", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * update user with new about me and image URL
     * @param microUser user object that contains the information that needs to be persisted to the current user
     * @return
     */
    @PutMapping("/about")
    public ResponseEntity<String> updateProfile(@RequestBody MicroUser microUser){
        MicroUser user = JwtUtil.extractUser(userService);
        user.setAbout(microUser.getAbout());
        user.setImageURL(microUser.getImageURL());
        try{
            return ResponseEntity.ok().body(
                    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
                            userService.updateProfile(user)
                    )
            );
        } catch (Exception e){
            log.error("Failed to update user.", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * update the current user's following list to include user with given user id
     * @param follow_id user id for the user that will be followed by current user
     * @return
     */
    @PutMapping("/follow/{follow_id}")
    public ResponseEntity<String> followUser(@PathVariable int follow_id) {
        MicroUser user = JwtUtil.extractUser(userService);

        try {
            log.info("Attempting to follow...");
            return ResponseEntity.ok().body(
                    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
                            userService.followUser(user, follow_id)));
        } catch (JsonProcessingException e) {
            log.error("Fail to write...", e);
            return ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            log.error("Fail to follow...", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * update the current user's following list to remove user with given user id
     * @param follow_id user id for the user that was being followed by current user
     * @return
     */
    @PutMapping("/unfollow/{follow_id}")
    public ResponseEntity<String> unfollowUser(@PathVariable int follow_id) {
        MicroUser user = JwtUtil.extractUser(userService);

        try {
            return ResponseEntity.ok().body(
                    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
                            userService.unfollowUser(user, follow_id)));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
