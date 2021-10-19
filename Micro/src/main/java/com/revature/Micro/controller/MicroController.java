package com.revature.Micro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.Micro.Entity.Micro;
import com.revature.Micro.Entity.MicroUser;
import com.revature.Micro.dto.MicroDTO;
import com.revature.Micro.dto.MicroUserDTO;
import com.revature.Micro.service.MicroService;
import com.revature.Micro.service.UserService;
import com.revature.Micro.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/micro")
public class MicroController {

    @Autowired
    private final MicroService microService;

    @Autowired
    private final UserService userService;

    public MicroController(MicroService microService, UserService userService) {
        this.microService = microService;
        this.userService = userService;
    }

    /**
     * Get a micro object by ID
     * @param id unique primary key of a micro(post)
     * @return a single micro object
     */
    @GetMapping("/{id}")
    public Micro getMicroById(@PathVariable int id) {
        return microService.getMicroById(id);
    }

    /**
     * Get a list of micros with current user and other
     * users followed
     * @return a list of micro objects
     */
    @GetMapping()
    public List<Micro> getAllMicrosByUserAndFollowing() {
        MicroUser user = JwtUtil.extractUser(userService);
        return microService.getAllMicros(user);
    }

    /**
     * Persist the microDTO to the database
     * @param microDTO a micro object passed in by the front end
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<String> createMicro(@RequestBody MicroDTO microDTO) {
        MicroUser microUser = JwtUtil.extractUser(userService);
        try {
            return ResponseEntity.ok().body(
                    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
                            microService.saveMicro(
                                    microDTO.convertToEntity(microUser))));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

}
