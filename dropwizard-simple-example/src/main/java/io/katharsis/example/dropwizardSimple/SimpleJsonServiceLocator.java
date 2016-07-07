package io.katharsis.example.dropwizardSimple;

import io.katharsis.locator.JsonServiceLocator;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class SimpleJsonServiceLocator implements JsonServiceLocator {

    private Map<Class, Object> map;

    public SimpleJsonServiceLocator() {
        map = new HashMap<>();
    }

    public <T> void register(T instance) {
        register(instance.getClass(), instance);
    }

    public <T> void register(Class<? extends T> clazz, T instance) {
        map.put(clazz, instance);
    }

    @Override
    public <T> T getInstance(Class<T> clazz) {
        if (map.containsKey(clazz)) {
            return clazz.cast(map.get(clazz));
        } else {
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}