package com.projecthub.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import com.projecthub.enums.ProjectStage;

@Entity
@Getter
@Setter
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String smallDescription;

    private List<String> tags;

    private String creator;
    private ProjectStage stage;
    private List<String> lookingFor;

    private String fullDescription;

    public Project() {

    }

    public void setStage(ProjectStage stage){
        this.stage = stage;
    }
    public void setStage(String stage){
        this.stage = ProjectStage.valueOf(stage.toUpperCase().replace(" ", "_"));
    }
}
