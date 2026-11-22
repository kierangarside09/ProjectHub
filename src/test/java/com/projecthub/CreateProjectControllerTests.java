package com.projecthub;

import com.projecthub.controllers.createProject.createProjectController;
import com.projecthub.entities.Project;
import com.projecthub.repositories.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CreateProjectControllerTests {

    private final ProjectRepository projectRepository = mock(ProjectRepository.class);
    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new createProjectController(projectRepository))
            .build();

    @Test
    void createsProjectAndRedirectsToBrowser() throws Exception {
        mockMvc.perform(post("/create")
                        .param("projectName", "ProjectHub")
                        .param("projectDescription", "Find collaborators"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/browser"));

        ArgumentCaptor<Project> project = ArgumentCaptor.forClass(Project.class);
        verify(projectRepository).save(project.capture());
        assertEquals("ProjectHub", project.getValue().getName());
        assertEquals("Find collaborators", project.getValue().getSmallDescription());
    }

    @Test
    void rejectsMissingProjectName() throws Exception {
        mockMvc.perform(post("/create")
                        .param("projectDescription", "Find collaborators"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(projectRepository);
    }
}
