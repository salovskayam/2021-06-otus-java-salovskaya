package ru.otus.jdbc.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.crm.model.Manager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Collectors;


class ManagerEntityClassMetaDataImplTest {

    private EntityClassMetaData<Manager> entityClassMetaData;

    @BeforeEach
    void setUp() {
        entityClassMetaData = new EntityClassMetaDataImpl<>(Manager.class);
    }

    @Test
    void getName() {
        Assertions.assertEquals("manager", entityClassMetaData.getName());
    }

    @Test
    void getConstructor() throws InvocationTargetException, InstantiationException, IllegalAccessException {
            Assertions.assertTrue(entityClassMetaData.getConstructor().newInstance() instanceof Manager);
    }

    @Test
    void getIdField() {
        Assertions.assertEquals("no" , entityClassMetaData.getIdField().getName());
    }

    @Test
    void getAllFields() {

        Assertions.assertEquals("label, no, param1",
            entityClassMetaData.getAllFields().stream()
                    .map(Field::getName)
                    .sorted()
                    .collect(Collectors.joining(", ")));

    }

    @Test
    void getFieldsWithoutId() {
        Assertions.assertEquals("label, param1",
            entityClassMetaData.getFieldsWithoutId().stream()
                    .map(Field::getName)
                    .sorted()
                    .collect(Collectors.joining(", ")));

    }
}