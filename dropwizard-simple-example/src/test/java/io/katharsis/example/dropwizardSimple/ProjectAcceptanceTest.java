package io.katharsis.example.dropwizardSimple;

import com.github.jasminb.jsonapi.retrofit.JSONAPIConverterFactory;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.katharsis.example.dropwizardSimple.domain.repository.ProjectRepository;
import io.katharsis.example.dropwizardSimple.domain.repository.TaskRepository;
import io.katharsis.example.dropwizardSimple.jsonapi.model.Project;
import io.katharsis.example.dropwizardSimple.jsonapi.model.Task;
import io.katharsis.example.dropwizardSimple.jsonapi.service.ProjectService;
import io.katharsis.example.dropwizardSimple.jsonapi.service.TaskService;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import retrofit.Response;
import retrofit.Retrofit;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This is an example test
 */
public class ProjectAcceptanceTest {

    @ClassRule
    public static final DropwizardAppRule<DropwizardConfiguration> RULE =
            new DropwizardAppRule<>(DropwizardService.class, ResourceHelpers.resourceFilePath("test-configuration.yml"));

    private static ProjectService projectService;
    private static ProjectRepository projectRepository;

    private static TaskService taskService;
    private static TaskRepository taskRepository;

    @BeforeClass
    public static void setUp() throws Exception {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:" + RULE.getLocalPort())
                .addConverterFactory(new JSONAPIConverterFactory(RULE.getObjectMapper(), Project.class, Task.class))
                .build();

        projectService = retrofit.create(ProjectService.class);
        projectRepository = ((DropwizardService)RULE.getApplication()).getInstance(ProjectRepository.class);

        taskService = retrofit.create(TaskService.class);
        taskRepository = ((DropwizardService)RULE.getApplication()).getInstance(TaskRepository.class);

        io.katharsis.example.dropwizardSimple.domain.model.Project project = new io.katharsis.example.dropwizardSimple.domain.model.Project();
        project.setName("simpleProject");
        projectRepository.save(project);

        io.katharsis.example.dropwizardSimple.domain.model.Task task = new io.katharsis.example.dropwizardSimple.domain.model.Task();
        task.setName("simpleTask");
        taskRepository.save(task);

        project.setTasks(Arrays.asList(task));
        task.setProject(project);
        projectRepository.save(project);
        taskRepository.save(task);
    }

    @Test
    public void should_be_able_to_query_projects_and_tasks_should_not_be_loaded() throws Exception {

        Response<List<Project>> response = projectService.find().execute();

        assertThat(response.isSuccess()).isTrue();

        List<Project> projects = response.body();

        assertThat(projects).hasSize(1);
        assertThat(projects).extracting("name").contains("simpleProject");
        assertThat(projects).extracting("tasks").isNotEmpty();
        assertThat(projects).extracting("tasks").containsNull();
    }

    @Test
    public void should_be_able_to_query_tasks_and_project_should_be_loaded_but_not_populated() throws Exception {

        Response<List<Task>> response = taskService.find().execute();

        assertThat(response.isSuccess()).isTrue();

        List<Task> tasks = response.body();

        assertThat(tasks).hasSize(1);
        assertThat(tasks).extracting("name").contains("simpleTask");
        assertThat(tasks).extracting("project").isNotEmpty();
        assertThat(tasks).extracting("project.name").containsNull();
    }
}
