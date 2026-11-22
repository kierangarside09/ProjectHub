package com.projecthub.controllers.browser;

import com.projecthub.entities.Project;
import com.projecthub.repositories.ProjectRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class browserController {

    private ProjectRepository projectRepository;

    public browserController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping("/browser")
    public String browser(Model model) {
        model.addAttribute("projects", projectRepository.findAll());
        return "browser/index";
    }
}
