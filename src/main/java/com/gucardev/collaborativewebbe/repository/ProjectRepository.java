package com.gucardev.collaborativewebbe.repository;

import com.gucardev.collaborativewebbe.model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends MongoRepository<Project,String> {

    Optional<Project> findByRoom(String room);

}
