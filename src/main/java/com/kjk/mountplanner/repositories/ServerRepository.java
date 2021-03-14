package com.kjk.mountplanner.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kjk.mountplanner.models.Server;

@Repository
public interface ServerRepository extends MongoRepository<Server, String>{

}
