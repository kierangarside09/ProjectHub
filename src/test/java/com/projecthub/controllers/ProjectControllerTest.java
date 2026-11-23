package com.projecthub.controllers;

import com.projecthub.config.SecurityConfig;
import com.projecthub.entities.Project;
import com.projecthub.repositories.AccountRepository;
import com.projecthub.repositories.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.ServletWebSecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@WebMvcTest(ProjectController.class)
@Import(SecurityConfig.class)
@ImportAutoConfiguration(ServletWebSecurityAutoConfiguration.class)
class ProjectControllerTest {

    private MockMvc mockMvc;

    @MockitoBean
    private ProjectRepository projectRepository;

    @MockitoBean
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        mockMvc = webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void anonymousUserIsRedirectedToLoginForCreateForm() throws Exception {
        mockMvc.perform(get("/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void authenticatedUserCanViewCreateForm() throws Exception {
        mockMvc.perform(get("/create").with(user("kiera")))
                .andExpect(status().isOk())
                .andExpect(view().name("createProject/index"));
    }

    @Test
    void authenticatedUserCanCreateProject() throws Exception {
        mockMvc.perform(post("/create")
                        .with(user("kiera"))
                        .with(csrf())
                        .param("name", "ProjectHub")
                        .param("smallDescription", "Find collaborators"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/browser"));

        ArgumentCaptor<Project> project = ArgumentCaptor.forClass(Project.class);
        verify(projectRepository).save(project.capture());
        assertEquals("ProjectHub", project.getValue().getName());
        assertEquals("Find collaborators", project.getValue().getSmallDescription());
    }

    @Test
    void postWithoutCsrfTokenIsForbidden() throws Exception {
        mockMvc.perform(post("/create")
                        .with(user("kiera"))
                        .param("name", "ProjectHub")
                        .param("smallDescription", "Find collaborators"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(projectRepository);
    }

    @Test
    void anonymousUserCannotCreateProject() throws Exception {
        mockMvc.perform(post("/create")
                        .with(csrf())
                        .param("name", "ProjectHub")
                        .param("smallDescription", "Find collaborators"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verifyNoInteractions(projectRepository);
    }
}
