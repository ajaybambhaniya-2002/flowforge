package com.flowforge.project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateProjectRequest {
    @NotBlank(message = "Project name is required")
    @Size(max=150,message = "Project name must not exceed 150 characters")
   private String name;

    @NotBlank(message = "Project key is required")
    @Size(max = 100, message = "Project key must not exceed 100 characters")
    private String projectKey;
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

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

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }


}
