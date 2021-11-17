package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class AppComponentsContainerImpl implements AppComponentsContainer {
//    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();
    private final Map<Class<?>, Object> appComponentsByClassName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        Object configClassInstance;
        try {
            configClassInstance = configClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<Method> methods = Arrays.stream(configClass.getMethods())
            .filter(s -> s.isAnnotationPresent(AppComponent.class))
            .sorted(Comparator.comparingInt(s -> s.getAnnotation(AppComponent.class).order()))
            .collect(Collectors.toUnmodifiableList());

        for (Method method : methods) {
            Object obj;
            Object[] params;
            if (method.getParameterCount() == 0) {
                try {
                    obj = method.invoke(configClassInstance);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                params = Arrays.stream(method.getParameters())
                    .map(m -> appComponentsByClassName.get(m.getType()))
                    .toArray();
                try {
                    obj = method.invoke(configClassInstance, params);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            appComponentsByName.put(method.getAnnotation(AppComponent.class).name(), obj);
            appComponentsByClassName.put(method.getReturnType(), obj);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        if (appComponentsByClassName.containsKey(componentClass)) {
            return (C) appComponentsByClassName.get(componentClass);
        }

        return Arrays.stream(componentClass.getInterfaces())
                    .filter(appComponentsByClassName::containsKey)
                    .findFirst()
                    .map(clazz -> (C) appComponentsByClassName.get(clazz)).orElse(null);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}
