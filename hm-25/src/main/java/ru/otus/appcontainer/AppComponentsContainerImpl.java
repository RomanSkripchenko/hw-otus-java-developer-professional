package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.appcontainer.api.AppComponentsContainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();
    private final Map<Class<?>, Object> appComponentsByClass = new HashMap<>(); // для поиска по классу

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        try {
            Object configInstance = configClass.getDeclaredConstructor().newInstance();

            List<Method> appComponentMethods = Arrays.stream(configClass.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(AppComponent.class))
                    .sorted(Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order()))
                    .collect(Collectors.toList());

            boolean[] componentsCreated = {true};
            do {
                componentsCreated[0] = false;
                for (Method method : appComponentMethods) {
                    AppComponent component = method.getAnnotation(AppComponent.class);
                    String componentName = component.name();

                    if (appComponentsByName.containsKey(componentName)) {
                        continue;
                    }

                    Object[] dependencies = Arrays.stream(method.getParameterTypes())
                            .map(dep -> appComponentsByClass.get(dep))
                            .toArray();

                    if (Arrays.stream(dependencies).allMatch(Objects::nonNull)) {
                        Object componentInstance = method.invoke(configInstance, dependencies);
                        registerComponent(componentName, componentInstance);
                        componentsCreated[0] = true;
                    }
                }
            } while (componentsCreated[0]);

        } catch (Exception e) {
            throw new RuntimeException("Error processing config class: " + configClass.getName(), e);
        }
    }

    private void registerComponent(String componentName, Object componentInstance) {
        if (appComponentsByName.containsKey(componentName)) {
            throw new IllegalStateException("Duplicate component name detected during registration: " + componentName);
        }

        appComponents.add(componentInstance);
        appComponentsByName.put(componentName, componentInstance);

        appComponentsByClass.put(componentInstance.getClass(), componentInstance);
        for (Class<?> iface : componentInstance.getClass().getInterfaces()) {
            appComponentsByClass.put(iface, componentInstance);
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        Object component = appComponentsByClass.get(componentClass);
        if (component == null) {
            throw new RuntimeException("Component not found for class: " + componentClass.getName());
        }
        return componentClass.cast(component);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C> C getAppComponent(String componentName) {
        Object component = appComponentsByName.get(componentName);
        if (component == null) {
            throw new RuntimeException("Component not found for name: " + componentName);
        }
        return (C) component;
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException("Given class is not a config class: " + configClass.getName());
        }
    }
}