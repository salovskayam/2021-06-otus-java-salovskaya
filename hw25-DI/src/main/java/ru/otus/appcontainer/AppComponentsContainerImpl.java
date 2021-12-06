package ru.otus.appcontainer;

import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import org.reflections.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class AppComponentsContainerImpl implements AppComponentsContainer {
    private static final Logger logger = LoggerFactory.getLogger(AppComponentsContainerImpl.class);

    private final Map<String, Object> appComponentsByName = new HashMap<>();
    private final Map<Class<?>, Object> appComponentsByClass = new HashMap<>();

    public AppComponentsContainerImpl(String packageName) {
        Class<?>[] classes = getAnnotatedClassesInPackage(packageName, AppComponentsContainerConfig.class);
        processConfig(classes);
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?>... initialConfigClasses) {
        // проверка, что все классы помечены аннотацией @AppComponentsContainerConfig
        Arrays.stream(initialConfigClasses).forEach(this::checkConfigClass);

        // сортируем классы в зависимости от свойства order аннотации @AppComponentsContainerConfig
        final List<Class<?>> configClasses = Arrays.stream(initialConfigClasses)
                .sorted(Comparator.comparingInt(s -> s.getAnnotation(AppComponentsContainerConfig.class).order()))
                .collect(Collectors.toUnmodifiableList());

        // подготавливаем инстансы классов, у которых затем будем вызывать методы для создания бинов
        final Map<String, Object> configClassInstances = configClasses.stream()
                .collect(Collectors.toUnmodifiableMap(Class::getSimpleName, clazz -> {
                    try {
                        return clazz.getConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }));

        // отсортированные методы из всех конфигов, помеченные аннотацией @AppComponent
        final List<Method> methods = configClasses.stream().flatMap(s -> Arrays.stream(s.getMethods()))
                .filter(s -> s.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(s -> s.getAnnotation(AppComponent.class).order()))
                .collect(Collectors.toUnmodifiableList());

        for (Method method : methods) {
            Object obj = callMethod(method, configClassInstances.get(method.getDeclaringClass().getSimpleName()));

            appComponentsByName.put(method.getAnnotation(AppComponent.class).name(), obj);
            appComponentsByClass.put(method.getReturnType(), obj);
            logger.info("Создан бин {}", obj.getClass().getSimpleName());
        }
        
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        if (appComponentsByClass.containsKey(componentClass)) {
            return (C) appComponentsByClass.get(componentClass);
        }

        return Arrays.stream(componentClass.getInterfaces())
                    .filter(appComponentsByClass::containsKey)
                    .findFirst()
                    .map(clazz -> (C) appComponentsByClass.get(clazz)).orElse(null);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }

    private Class<?>[] getAnnotatedClassesInPackage(String packageName, Class<? extends Annotation> annotation) {
        List<ClassLoader> classLoadersList = new LinkedList<>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false), new TypeAnnotationsScanner())
                .setUrls(ClasspathHelper.forPackage(packageName))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageName))));

        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(annotation);
        return annotated.toArray(Class<?>[]::new);
    }

    private Object callMethod(Method method, Object configClassInstance) {
        Object obj;

        Object[] params = Arrays.stream(method.getParameters()).map(m -> {
            if (appComponentsByClass.containsKey(m.getType())) {
                return appComponentsByClass.get(m.getType());
            } else {
                throw new RuntimeException(String.format("Отсутствует бин %s", m.getType().getSimpleName()));
            }
        }).toArray();

        try {
            obj = method.invoke(configClassInstance, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        return obj;
    }
}
