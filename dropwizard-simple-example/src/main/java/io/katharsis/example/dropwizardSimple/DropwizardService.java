package io.katharsis.example.dropwizardSimple;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import io.katharsis.example.dropwizardSimple.domain.repository.ProjectRepository;
import io.katharsis.example.dropwizardSimple.domain.repository.TaskRepository;
import io.katharsis.example.dropwizardSimple.domain.repository.TaskToProjectRepository;
import io.katharsis.queryParams.DefaultQueryParamsParser;
import io.katharsis.queryParams.QueryParamsBuilder;
import io.katharsis.rs.KatharsisFeature;

import static io.katharsis.rs.KatharsisProperties.RESOURCE_DEFAULT_DOMAIN;
import static io.katharsis.rs.KatharsisProperties.RESOURCE_SEARCH_PACKAGE;

public class DropwizardService extends Application<DropwizardConfiguration> {

    private SimpleJsonServiceLocator jsonServiceLocator;

    public DropwizardService() {
        jsonServiceLocator = new SimpleJsonServiceLocator();
    }

    /**
     * Uses the lookup
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getInstance(Class<T> clazz) {
        return jsonServiceLocator.getInstance(clazz);
    }


    @Override
    public void run(DropwizardConfiguration dropwizardConfiguration, Environment environment) throws Exception {

        environment.jersey().property(RESOURCE_DEFAULT_DOMAIN, dropwizardConfiguration.katharsis.host);
        environment.jersey().property(RESOURCE_SEARCH_PACKAGE, dropwizardConfiguration.katharsis.searchPackage);

        final ProjectRepository projectRepository = new ProjectRepository();
        final TaskRepository taskRepository = new TaskRepository();
        final TaskToProjectRepository taskToProjectRepository = new TaskToProjectRepository(taskRepository, projectRepository);

        jsonServiceLocator.register(projectRepository);
        jsonServiceLocator.register(taskRepository);
        jsonServiceLocator.register(taskToProjectRepository);

        KatharsisFeature katharsisFeature = new KatharsisFeature(environment.getObjectMapper(),
                new QueryParamsBuilder(new DefaultQueryParamsParser()),
                jsonServiceLocator);
        environment.jersey().register(katharsisFeature);
    }

    public static void main(String[] args) throws Exception {
        new DropwizardService().run(args);
    }
}
