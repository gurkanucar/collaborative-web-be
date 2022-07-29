package com.gucardev.collaborativewebbe.service;

import com.gucardev.collaborativewebbe.constant.DefaultProjectValues;
import com.gucardev.collaborativewebbe.exception.GeneralException;
import com.gucardev.collaborativewebbe.model.Project;
import com.gucardev.collaborativewebbe.repository.ProjectRepository;
import com.gucardev.collaborativewebbe.util.ProjectConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectConverter projectConverter;

    public ProjectService(ProjectRepository projectRepository,
                          ProjectConverter projectConverter) {
        this.projectRepository = projectRepository;
        this.projectConverter = projectConverter;
    }

    public Project getProjectByID(String id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new GeneralException("Not found!", HttpStatus.NOT_FOUND));
    }

    public Project getProjectByRoom(String room) {
        return projectRepository.findByRoom(room)
                .orElseThrow(() -> new GeneralException("Not found!", HttpStatus.NOT_FOUND));
    }

    public boolean existsByRoom(String room) {
        return projectRepository.findByRoom(room).isPresent();
    }

    public Project create(Project project) {
        if (existsByRoom(project.getRoom()))
            throw new GeneralException("Already exists!", HttpStatus.CONFLICT);
        log.info("Project saved");
        return projectRepository.save(project);
    }

    public Project getOrCreateByDefaultValues(String room) {
        if (existsByRoom(room)) {
            log.info("Project already exists");
            return getProjectByRoom(room);
        }
        var project = Project.builder()
                .room(room)
                .html(DefaultProjectValues.HTML)
                .css(DefaultProjectValues.CSS)
                .js(DefaultProjectValues.JS)
                .build();
        log.info("Project saved");
        return create(project);
    }


    public Project update(Project project) {
        if (!existsByRoom(project.getRoom())) {
            project.setHtml(DefaultProjectValues.HTML);
            project.setCss(DefaultProjectValues.CSS);
            project.setJs(DefaultProjectValues.JS);
            return create(project);
        }
        var existing = getProjectByRoom(project.getRoom());
        existing.setHtml(project.getHtml());
        existing.setCss(project.getCss());
        existing.setJs(project.getJs());
        log.info("Project updated");
        return projectRepository.save(existing);
    }


    public String projectToJsonString(Project project) {
        return projectConverter.convertToJsonString(project);
    }

    public String projectToJsonString(List<Project> projectList) {
        return projectConverter.convertToJsonString(projectList);
    }


}
