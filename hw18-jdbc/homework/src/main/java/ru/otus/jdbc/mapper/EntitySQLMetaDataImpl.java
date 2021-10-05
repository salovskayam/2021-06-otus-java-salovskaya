package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private static final Logger logger = LoggerFactory.getLogger(EntitySQLMetaDataImpl.class);

    private final EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {

        return "select " + entityClassMetaData.getAllFields().stream()
                            .map(Field::getName)
                            .sorted()
                            .collect(Collectors.joining(", "))
                + " from " + entityClassMetaData.getName();

    }

    @Override
    public String getSelectByIdSql() {

        return "select " + entityClassMetaData.getFieldsWithoutId().stream()
                                .map(Field::getName)
                                .sorted()
                                .collect(Collectors.joining(", "))
                + " from " + entityClassMetaData.getName()
                + " where " + entityClassMetaData.getIdField().getName()
                + " = ?";

    }

    @Override
    public String getInsertSql() {
        StringBuilder builder = new StringBuilder("?");
        builder.append(", ?".repeat(Math.max(0, entityClassMetaData.getFieldsWithoutId().size() - 1)));

        return "insert into " + entityClassMetaData.getName()
                + "(" + entityClassMetaData.getFieldsWithoutId().stream()
                            .map(Field::getName)
                            .sorted()
                            .collect(Collectors.joining(", "))
                + ") values (" + builder
                + ")";

    }

    @Override
    public String getUpdateSql() {
        String update = "update " + entityClassMetaData.getName()
                + " set " + entityClassMetaData.getFieldsWithoutId().stream()
                    .map(f -> f.getName().concat(" = ?"))
                    .sorted()
                    .collect(Collectors.joining(", "))
                + " where " + entityClassMetaData.getIdField().getName()
                + " = ?";

        logger.info("query: {}", update);

        return update;

    }

}
