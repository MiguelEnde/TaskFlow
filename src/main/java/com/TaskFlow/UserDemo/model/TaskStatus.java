package com.taskflow.model;

public enum TaskStatus {
    TODO("Por Hacer"),
    IN_PROGRESS("En Proceso"),
    DONE("Completado");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
