package com.revature.Micro.controller;

import com.revature.Micro.Entity.Micro;
import com.revature.Micro.service.MicroService;
import com.revature.Micro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/micro")
public class MicroController {

    @Autowired
    private final MicroService microService;

//    @Autowired
//    private final UserService userService;

    public MicroController(MicroService microService) {
        this.microService = microService;
//        this.userService = userService;
    }

    @GetMapping("/{id}")
    public Micro getMicroById(@PathVariable int id) {
        return microService.getMicroById(id);
    }
    @GetMapping("/api")
    public @ResponseBody
    List<Micro> getAllMicros(){
        return microService.getAllMicros();
    }

//    @GetMapping("/{userId}")
//    public List<Micro> getMicrosByUser(@PathVariable String userId) {
//        return null;
//    }

    @PostMapping("/post")
    public ResponseEntity<String> createMicro(@RequestBody Micro micro) {
        microService.saveMicro(micro);
        return null;
    }

}
