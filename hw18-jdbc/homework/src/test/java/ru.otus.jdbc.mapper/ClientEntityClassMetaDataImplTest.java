package ru.otus.jdbc.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.crm.model.Client;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Collectors;


class ClientEntityClassMetaDataImplTest {

    private EntityClassMetaData<Client> entityClassMetaData;

    @BeforeEach
    void setUp() {
        entityClassMetaData = new EntityClassMetaDataImpl<>(Client.class);
    }

    @Test
    void getName() {
        Assertions.assertEquals("client", entityClassMetaData.getName());
    }

    @Test
    void getConstructor() throws InvocationTargetException, InstantiationException, IllegalAccessException {
            Assertions.assertTrue(entityClassMetaData.getConstructor().newInstance() instanceof Client);
    }

    @Test
    void getIdField() {
        Assertions.assertEquals("id" , entityClassMetaData.getIdField().getName());
    }

    @Test
    void getAllFields() {

        Assertions.assertEquals("id, name",
            entityClassMetaData.getAllFields().stream()
                    .map(Field::getName)
                    .sorted()
                    .collect(Collectors.joining(", ")));

    }

    @Test
    void getFieldsWithoutId() {
        Assertions.assertEquals("name",
            entityClassMetaData.getFieldsWithoutId().stream()
                    .map(Field::getName)
                    .sorted()
                    .collect(Collectors.joining(", ")));

    }
}