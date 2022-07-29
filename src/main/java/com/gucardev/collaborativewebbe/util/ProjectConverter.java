package com.gucardev.collaborativewebbe.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gucardev.collaborativewebbe.exception.GeneralException;
import com.gucardev.collaborativewebbe.model.Project;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectConverter {

    public String convertToJsonString(Project project) {
        try {
            return new ObjectMapper().writeValueAsString(project);
        } catch (JsonProcessingException e) {
            throw new GeneralException("json deserialization error", HttpStatus.BAD_REQUEST);
        }
    }

    public String convertToJsonString(List<Project> projectList) {
        try {
            return new ObjectMapper().writeValueAsString(projectList);
        } catch (JsonProcessingException e) {
            throw new GeneralException("json deserialization error", HttpStatus.BAD_REQUEST);
        }
    }

}
