package com.kjk.mountplanner.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kjk.mountplanner.models.Mount;

@Repository
public interface MountRepository extends MongoRepository<Mount, String> {
	List<Mount> findByNameNotIn(List<String> names);
	
}
