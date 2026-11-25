package com.projecthub.controllers.createProject;

import java.util.List;

import com.projecthub.entities.Project;
import com.projecthub.repositories.ProjectRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class createProjectController {

    private final ProjectRepository projectRepository;

    public createProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping("/create")
    public String index() {
        return "createProject/index";
    }

    @PostMapping("/create")
    public String createProjectPost(@RequestParam("name") String name,
                                    @RequestParam("smallDescription") String smallDescription,
                                    @RequestParam("tags") List<String> tags,
                                    @RequestParam("creator") String creator,
                                    @RequestParam("stage") String stage,
                                    @RequestParam("lookingFor") List<String> lookingFor,
                                    @RequestParam("fullDescription") String fullDescription) {
        Project project = new Project();
        project.setName(name);
        project.setSmallDescription(smallDescription);
        project.setTags(tags);
        project.setCreator(creator);
        project.setStage(stage);
        project.setLookingFor(lookingFor);
        project.setFullDescription(fullDescription);
        projectRepository.save(project);
        return "redirect:/browser";
    }
}
