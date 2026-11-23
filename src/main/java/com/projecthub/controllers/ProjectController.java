package com.projecthub.controllers;

import com.projecthub.entities.Project;
import com.projecthub.repositories.ProjectRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProjectController {

    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping("/create")
    public String index() {
        return "createProject/index";
    }

    @PostMapping("/create")
    public String createProjectPost(@RequestParam("name") String projectName,
                                    @RequestParam("smallDescription") String projectDescription) {
        Project project = new Project();
        project.setName(projectName);
        project.setSmallDescription(projectDescription);
        projectRepository.save(project);
        return "redirect:/browser";
    }
}
