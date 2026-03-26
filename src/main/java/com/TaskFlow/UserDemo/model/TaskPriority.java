package com.taskflow.model;

public enum TaskPriority {
    LOW("Baja"),
    MEDIUM("Media"),
    HIGH("Alta");

    private final String displayName;

    TaskPriority(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
