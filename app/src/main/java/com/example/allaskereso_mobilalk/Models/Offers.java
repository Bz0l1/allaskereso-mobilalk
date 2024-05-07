package com.example.allaskereso_mobilalk.Models;

public class Offers {
    private final String id, name, employerName, description;
    private boolean applied;

    public Offers(String id, String name, String employerName, String description, boolean applied) {
        this.id = id;
        this.name = name;
        this.employerName = employerName;
        this.description = description;
        this.applied = applied;
    }

    public String getName() {
        return name;
    }

    public String getEmployerName() {
        return employerName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isApplied() {
        return applied;
    }

    public void setApplied(boolean applied) {
        this.applied = applied;
    }

    public String getId() {
        return id;
    }
}
