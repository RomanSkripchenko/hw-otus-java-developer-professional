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
        System.out.println("Initializing AppComponentsContainerImpl for " + initialConfigClass.getSimpleName());
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

            for (Method method : appComponentMethods) {
                AppComponent component = method.getAnnotation(AppComponent.class);
                String componentName = component.name();
                Object[] dependencies = Arrays.stream(method.getParameterTypes())
                        .map(dep -> appComponentsByClass.get(dep))
                        .toArray();

                // Проверка на наличие всех зависимостей перед регистрацией
                if (Arrays.stream(dependencies).allMatch(Objects::nonNull)) {
                    Object componentInstance = method.invoke(configInstance, dependencies);

                    // Попытка регистрации компонента; если имя дублируется, выбрасывается исключение
                    registerComponent(componentName, componentInstance);
                } else {
                    throw new RuntimeException("Unable to resolve dependencies for component: " + componentName);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing config class: " + configClass.getName(), e);
        }
    }


    private void registerComponent(String componentName, Object componentInstance) {
        System.out.println("Attempting to register component: " + componentName);
        if (appComponentsByName.containsKey(componentName)) {
            throw new IllegalStateException("Duplicate component name detected: " + componentName);
        }
        System.out.println("Registering component: " + componentName);
        appComponents.add(componentInstance);
        appComponentsByName.put(componentName, componentInstance);
        appComponentsByClass.put(componentInstance.getClass(), componentInstance);

        for (Class<?> interfaceClass : componentInstance.getClass().getInterfaces()) {
            appComponentsByClass.put(interfaceClass, componentInstance);
        }
    }





    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(Class<C> componentClass) {
        System.out.println("Looking for component with class: " + componentClass.getName());
        Object foundComponent = null;
        for (Object component : appComponents) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                if (foundComponent != null) {
                    throw new RuntimeException("More than one component found for class: " + componentClass.getName());
                }
                foundComponent = component;
            }
        }
        if (foundComponent == null) {
            throw new RuntimeException("Component not found for class: " + componentClass.getName());
        }
        return (C) foundComponent;
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