package io.katharsis.example.dropwizardSimple.jsonapi.service;


import io.katharsis.example.dropwizardSimple.jsonapi.model.Task;
import retrofit.Call;
import retrofit.http.GET;

import java.util.List;

/**
 * A retrofit interface for querying tasks
 */
public interface TaskService {

    @GET("/tasks")
    Call<List<Task>> find();
}
