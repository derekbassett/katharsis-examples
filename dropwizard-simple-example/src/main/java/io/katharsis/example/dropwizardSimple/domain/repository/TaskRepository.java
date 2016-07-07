package io.katharsis.example.dropwizardSimple.domain.repository;

import com.google.common.collect.Iterables;
import io.katharsis.example.dropwizardSimple.domain.model.Task;
import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.ResourceRepository;
import io.katharsis.resource.exception.ResourceNotFoundException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class TaskRepository implements ResourceRepository<Task, Long> {

    private static final Map<Long, Task> REPOSITORY = new ConcurrentHashMap<>();
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    public <S extends Task> S save(S entity) {
        if (entity.getId() == null) {
            entity.setId(ID_GENERATOR.getAndIncrement());
        }
        REPOSITORY.put(entity.getId(), entity);
        return entity;
    }

    public Task findOne(Long id, QueryParams requestParams) {
        Task task = REPOSITORY.get(id);
        if (task == null) {
            throw new ResourceNotFoundException("Project not found");
        }
        return task;
    }

    @Override
    public Iterable<Task> findAll(QueryParams requestParams) {
        return REPOSITORY.values();
    }

    @Override
    public Iterable<Task> findAll(Iterable<Long> iterable, QueryParams requestParams) {
        return REPOSITORY.entrySet()
                .stream()
                .filter(p -> Iterables.contains(iterable, p.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                .values();
    }

    public void delete(Long id) {
        REPOSITORY.remove(id);
    }
}