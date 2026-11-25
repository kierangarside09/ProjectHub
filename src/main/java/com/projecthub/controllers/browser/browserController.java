package com.projecthub.controllers.browser;

import java.util.List;

import com.projecthub.entities.Project;
import com.projecthub.enums.ProjectStage;
import com.projecthub.repositories.ProjectRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class browserController {

    private final ProjectRepository projectRepository;

    public browserController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping("/browser")
    public String browser(Model model) {
        //model.addAttribute("projects", projectRepository.findAll());
        Project project = new Project();
        project.setName("Factorio Base");
        project.setSmallDescription("Kieran is slacking on the base");
        project.setCreator("Kieran Garfield");
        project.setTags(List.of("Factorio", "Slacking", "Trello Board"));
        project.setStage(ProjectStage.ALPHA_BUILD);
        project.setLookingFor(List.of("Builders", "Beginners"));
        project.setFullDescription("Kieran keeps slacking on the base");

        model.addAttribute("projects", List.of(project));
        return "browser/index";
    }
}