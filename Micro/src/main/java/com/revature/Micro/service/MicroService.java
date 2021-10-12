package com.revature.Micro.service;

import com.revature.Micro.Entity.Micro;
//import com.revature.Micro.Entity.User;
import com.revature.Micro.repository.MicroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

//    /**
//     * Retrieves all Micro objects in the database that contain a specific User object reference
//     * and puts them into a List
//     * @param user User object to search
//     * @return a List of Micro objects
//     */
//    public List<Micro> getMicrosByUser(User user){
//        return microRepository.getMicrosByUser(user);
//    }

    /**
     * Retrieves all Micro objects in the database and returns them in a List
     * @return a List of Micro objects
     */
    public List<Micro> getAllMicros(){
        return microRepository.findAll();
    }

    /**
     * Finds a specified Micro object in the database by its microId and
     * deletes it if it exists.
     * @param microId id of Micro
     */
    public void deleteMicro (Integer microId){
        microRepository.findById(microId).ifPresent(microRepository::delete);
    }
}
