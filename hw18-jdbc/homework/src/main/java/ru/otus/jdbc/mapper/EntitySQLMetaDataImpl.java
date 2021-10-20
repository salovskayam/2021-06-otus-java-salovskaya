package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private static final Logger logger = LoggerFactory.getLogger(EntitySQLMetaDataImpl.class);

    private final EntityClassMetaData<T> entityClassMetaData;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;

        this.allFields = entityClassMetaData.getAllFields();
        this.fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
    }

    @Override
    public String getSelectAllSql() {

        return "select " + allFields.stream()
                            .map(Field::getName)
                            .sorted()
                            .collect(Collectors.joining(", "))
                + " from " + entityClassMetaData.getName();

    }

    @Override
    public String getSelectByIdSql() {

        return "select " + fieldsWithoutId.stream()
                                .map(Field::getName)
                                .sorted()
                                .collect(Collectors.joining(", "))
            + " from " + entityClassMetaData.getName()
            + " where " + entityClassMetaData.getIdField().getName()
            + " = ?";

    }

    @Override
    public String getInsertSql() {
        String builder = "?" + ", ?".repeat(Math.max(0, this.fieldsWithoutId.size() - 1));

        return "insert into " + entityClassMetaData.getName()
            + "(" + fieldsWithoutId.stream()
                        .map(Field::getName)
                        .sorted()
                        .collect(Collectors.joining(", "))
            + ") values (" + builder
            + ")";

    }

    @Override
    public String getUpdateSql() {
        String update = "update " + entityClassMetaData.getName()
            + " set " + fieldsWithoutId.stream()
                            .map(f -> f.getName().concat(" = ?"))
                            .sorted()
                            .collect(Collectors.joining(", "))
            + " where " + entityClassMetaData.getIdField().getName()
            + " = ?";

        logger.info("query: {}", update);

        return update;

    }

}
