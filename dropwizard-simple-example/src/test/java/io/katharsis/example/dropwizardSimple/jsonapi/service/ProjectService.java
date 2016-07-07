package io.katharsis.example.dropwizardSimple.jsonapi.service;

import io.katharsis.example.dropwizardSimple.jsonapi.model.Project;
import retrofit.Call;
import retrofit.http.GET;

import java.util.List;

/**
 * A retrofit service for querying project values
 */
public interface ProjectService {

    @GET("/projects")
    Call<List<Project>> find();
}
