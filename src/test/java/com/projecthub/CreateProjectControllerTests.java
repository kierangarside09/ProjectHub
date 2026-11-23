package com.projecthub;

import com.projecthub.controllers.ProjectController;
import com.projecthub.entities.Project;
import com.projecthub.repositories.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class CreateProjectControllerTests {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectController controller;

    @Test
    void createsProjectAndRedirectsToBrowser() {
        String view = controller.createProjectPost("ProjectHub", "Find collaborators");

        assertEquals("redirect:/browser", view);
        ArgumentCaptor<Project> project = ArgumentCaptor.forClass(Project.class);
        verify(projectRepository).save(project.capture());
        assertNull(project.getValue().getId());
        assertEquals("ProjectHub", project.getValue().getName());
        assertEquals("Find collaborators", project.getValue().getSmallDescription());
    }
}
