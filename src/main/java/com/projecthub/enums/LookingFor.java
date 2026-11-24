package com.projecthub.enums;

import lombok.Getter;

@Getter
public enum LookingFor {
    DEVELOPER("Developer"),
    DESIGNER("Designer"),
    FRONTEND_DEVELOPER("Frontend Developer"),
    BACKEND_DEVELOPER("Backend Developer"),
    FULL_STACK_DEVELOPER("Full-Stack Developer"),
    MOBILE_DEVELOPER("Mobile Developer"),
    GAME_DEVELOPER("Game Developer"),
    DEVOPS_ENGINEER("DevOps Engineer"),
    DATA_SCIENTIST("Data Scientist"),
    QA_TESTER("QA / Tester"),
    PROJECT_MANAGER("Project Manager"),
    WRITER("Writer"),
    ARTIST("Artist"),
    MUSICIAN("Musician"),
    MARKETING("Marketing"),
    MENTOR("Mentor"),
    CO_FOUNDER("Co-founder"),
    ANYTHING("Anything"),
    NOT_LOOKING("Not looking right now");

    private final String value;

    LookingFor(String value) {
        this.value = value;
    }
}
