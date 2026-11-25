package com.projecthub;

import com.projecthub.controllers.createProject.createProjectController;
import com.projecthub.entities.Project;
import com.projecthub.enums.ProjectStage;
import com.projecthub.repositories.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class CreateProjectControllerTests {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private createProjectController controller;

    @Test
    void createsProjectAndRedirectsToBrowser() {
        String view = controller.createProjectPost("ProjectHub", "Find collaborators", List.of("Beginners Welcome", "Active"), "John Doe", "Early Prototype", List.of("Frontend", "DevOps"), "This is a full description of the project");

        assertEquals("redirect:/browser", view);
        ArgumentCaptor<Project> project = ArgumentCaptor.forClass(Project.class);
        verify(projectRepository).save(project.capture());
        assertNull(project.getValue().getId());
        assertEquals("ProjectHub", project.getValue().getName());
        assertEquals("Find collaborators", project.getValue().getSmallDescription());
        assertEquals(List.of("Beginners Welcome", "Active"), project.getValue().getTags());
        assertEquals("John Doe", project.getValue().getCreator());
        assertEquals(ProjectStage.EARLY_PROTOTYPE, project.getValue().getStage());
        assertEquals(List.of("Frontend", "DevOps"), project.getValue().getLookingFor());
        assertEquals("This is a full description of the project", project.getValue().getFullDescription());
    }
}
