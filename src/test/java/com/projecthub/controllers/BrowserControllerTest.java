package com.projecthub.controllers;

import com.projecthub.config.SecurityConfig;
import com.projecthub.entities.Project;
import com.projecthub.repositories.AccountRepository;
import com.projecthub.repositories.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.ServletWebSecurityAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BrowserController.class)
@Import(SecurityConfig.class)
@ImportAutoConfiguration(ServletWebSecurityAutoConfiguration.class)
class BrowserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectRepository projectRepository;

    @MockitoBean
    private AccountRepository accountRepository;

    @Test
    void anonymousUserCanBrowseProjects() throws Exception {
        Project project = new Project();
        project.setName("ProjectHub");
        project.setSmallDescription("Find collaborators");
        when(projectRepository.findAll()).thenReturn(List.of(project));

        mockMvc.perform(get("/browser"))
                .andExpect(status().isOk())
                .andExpect(view().name("browser/index"))
                .andExpect(model().attribute("projects", hasSize(1)));
    }

    @Test
    void browserShowsEmptyListWhenNoProjects() throws Exception {
        when(projectRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/browser"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("projects", hasSize(0)));
    }
}
