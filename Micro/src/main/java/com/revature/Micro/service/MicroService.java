package com.revature.Micro.service;

import com.revature.Micro.Entity.Micro;
//import com.revature.Micro.Entity.User;
import com.revature.Micro.Entity.MicroUser;
import com.revature.Micro.repository.MicroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MicroService {

    private final MicroRepository microRepository;

    @Autowired
    public MicroService(MicroRepository microRepository) {
        this.microRepository = microRepository;
    }

    /**
     * Adds or updates a Micro object if it already exists in the database,
     * then returns it.
     * @param micro a Micro object to save
     * @return a Micro object
     */
    public Micro saveMicro (Micro micro){
        return microRepository.save(micro);
    }

    /**
     * Searches the database for a specific Micro object based on its id
     * and returns it if it exists, otherwise throws an exception.
     * @param id Micro id
     * @return a Micro object or a RuntimeException
     */
    public Micro getMicroById (int id){
        return microRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    /**
     * Retrieves all Micro objects in the database and returns them in a List
     * @return a List of Micro objects
     */
    public List<Micro> getAllMicros(MicroUser user){
        return microRepository.getAllMicrosFromUserAndFollowingByUser(user.getId());
    }

}
