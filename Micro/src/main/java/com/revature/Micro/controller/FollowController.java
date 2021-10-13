package com.revature.Micro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.Micro.Entity.Follow;
import com.revature.Micro.Entity.Micro;
import com.revature.Micro.Entity.MicroUser;
import com.revature.Micro.dto.FollowDTO;
import com.revature.Micro.dto.MicroDTO;
import com.revature.Micro.dto.MicroUserDTO;
import com.revature.Micro.service.FollowService;
import com.revature.Micro.service.MicroService;
import com.revature.Micro.service.UserService;
import com.revature.Micro.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/follow")
public class FollowController {

    @Autowired
    private final FollowService followService;

    @Autowired
    private final UserService userService;

    public FollowController(FollowService followService, UserService userService) {
        this.followService = followService;
        this.userService = userService;
    }

    @GetMapping
    public @ResponseBody
    List<Follow> getAll() {
        return followService.getAll();
    }

    @PostMapping
    public ResponseEntity<String> follow(@RequestBody FollowDTO followDTO) {
        MicroUser followBy = JwtUtil.extractUser(userService);
        MicroUser following = new MicroUser();
        try {
            return ResponseEntity.ok().body(
                    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
                            followService.saveFollow(followDTO.convertToEntity(followBy, following))
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
