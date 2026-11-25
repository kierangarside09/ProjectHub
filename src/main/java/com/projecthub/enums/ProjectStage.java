package com.projecthub.enums;

public enum ProjectStage {
    EARLY_PROTOTYPE("Early Prototype"),
    ALPHA_BUILD("Alpha Build"),
    BETA_TESTING("Beta Testing"),
    EARLY_ACCESS("Early Access"),
    POST_LAUNCH("Post Launch"),
    ABANDONED_FACTORIO_SERVER("Abandoned Factorio Server");

    public final String value;

    ProjectStage(String value) {
        this.value = value;
    }
}
