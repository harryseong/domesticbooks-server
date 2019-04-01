package com.harryseong.mybookrepo.resources.dto;

public class PlanDTO {

    private String name;
    private String description;

    public PlanDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
