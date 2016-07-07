package io.katharsis.example.dropwizardSimple.jsonapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jasminb.jsonapi.annotations.Relationship;
import com.github.jasminb.jsonapi.annotations.Type;

/**
 *
 */
@Type("task")
public class Task extends BaseResource {

    @JsonProperty
    private String name;

    @Relationship("project")
    private Project project;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
