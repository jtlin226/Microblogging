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

    @PostMapping("/register")
    public MicroUser createUser(@RequestBody MicroUser microUser){
        log.info("New user being created");
        return userService.saveNewUser(microUser);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticateToken(@RequestBody AuthenticationRequest authReq){
        log.info("User attempting to login.");
        return userService.authenticate(authReq);
    }

    /**
     *
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

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody MicroUser microUser){
        try{
            return ResponseEntity.ok().body(
                    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
                            userService.updateUser(microUser)
                    )
            );
        } catch (Exception e){
            log.error("Failed to update user.", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
