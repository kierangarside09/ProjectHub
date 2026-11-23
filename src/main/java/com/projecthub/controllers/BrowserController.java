package com.projecthub.controllers;

import com.projecthub.repositories.ProjectRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BrowserController {

    private final ProjectRepository projectRepository;

    public BrowserController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping("/browser")
    public String index(Model model) {
        model.addAttribute("projects", projectRepository.findAll());
        return "browser/index";
    }
}
