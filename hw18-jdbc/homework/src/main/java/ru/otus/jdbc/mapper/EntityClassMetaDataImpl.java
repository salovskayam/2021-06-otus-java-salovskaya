package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplateException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final String entityName;
    private final Field id;
    private final Constructor<T> constructor;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) {

        this.constructor = getConstructorImpl(clazz);

        this.entityName = clazz.getSimpleName().toLowerCase();

        Field[] fields = clazz.getDeclaredFields();

        this.id = Arrays.stream(fields)
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst().orElse(null);

        this.allFields = Arrays.stream(fields)
                .collect(Collectors.toUnmodifiableList());

        this.fieldsWithoutId = Arrays.stream(fields)
                .filter(f -> !f.isAnnotationPresent(Id.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public String getName() {
        return entityName;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return id;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }

    private Constructor<T> getConstructorImpl(Class<T> clazz) {
        try {
            return clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new DataTemplateException(e);
        }
    }
}
